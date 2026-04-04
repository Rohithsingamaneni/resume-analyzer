package com.example.resume.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IngestionService {

    private final VectorStore vectorStore;

    // Looks for any PDF in your resources/resumes folder
    @Value("classpath:resumes/*.pdf")
    private Resource[] resumeFiles;

    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingest() {
        if (resumeFiles == null || resumeFiles.length == 0) {
            log.warn("No resumes found in resources/resumes/ folder!");
            return;
        }

        for (Resource pdfResource : resumeFiles) {
            log.info("Processing resume: {}", pdfResource.getFilename());

            // 1. EXTRACT: Read the PDF
            var pdfReader = new PagePdfDocumentReader(pdfResource);
            List<Document> documents = pdfReader.get();

            // 2. TRANSFORM: Split into chunks (so the AI doesn't get overwhelmed)
            // 800 tokens per chunk with a 100-token overlap to keep context
            var splitter = new TokenTextSplitter(800, 100, 5, 10000, true);
            List<Document> chunks = splitter.apply(documents);

            // 3. LOAD: Send to PGVector
            // This automatically calls Ollama to turn text into "numbers" (embeddings)
            vectorStore.accept(chunks);

            log.info("Successfully indexed {} chunks for {}", chunks.size(), pdfResource.getFilename());
        }
    }
}
