package com.example.resume.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.vectorstore.VectorStore;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestionServiceTest {

    @Mock
    private VectorStore vectorStore;

    @InjectMocks
    private IngestionService ingestionService;

    @Test
    void testIngest_ShouldCallVectorStore() {
        // Since we are unit testing, we just want to see if the service
        // logic flows correctly to the vectorStore.

        ingestionService.ingest();

        // Verify that vectorStore.accept() was called with a list of documents
        verify(vectorStore, atLeastOnce()).accept(anyList());
    }
}
