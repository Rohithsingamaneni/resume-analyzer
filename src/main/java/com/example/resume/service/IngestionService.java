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
    private final Resource[] resumeFiles;
    public IngestionService(VectorStore vectorStore,
                            @Value("${resumes.path:classpath:resumes/*.pdf}") Resource[] resumes) {
        this.vectorStore = vectorStore;
        this.resumeFiles = (resumes != null) ? resumes : new Resource[0];
    }

    public void ingest() {
        if (resumeFiles.length == 0) {
            log.warn("No resumes found in resources/resumes/ folder!");
            return;
        }

        for (Resource pdfResource : resumeFiles) {
            log.info("Processing resume: {}", pdfResource.getFilename());

            var pdfReader = new PagePdfDocumentReader(pdfResource);
            List<Document> documents = pdfReader.get();

            var splitter = new TokenTextSplitter(800, 100, 5, 10000, true);
            List<Document> chunks = splitter.apply(documents);
            vectorStore.accept(chunks);

            log.info("Successfully indexed {} chunks for {}", chunks.size(), pdfResource.getFilename());
        }
    }
}
