package com.example.resume.service;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IngestionService {
    private final VectorStore vectorStore;
    @Value("classpath:resumes/*.pdf")
    private Resource[] resumeFiles;

    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingest() {
        for (Resource res : resumeFiles) {
            var reader = new PagePdfDocumentReader(res);
            var splitter = new TokenTextSplitter();
            vectorStore.accept(splitter.apply(reader.get()));
        }
    }
}
