package com.example.resume.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestionServiceTest {

    @Mock
    private VectorStore vectorStore;

    @Test
    void testIngest_ShouldCallVectorStore_WhenFilesExist() {
        Resource dummyPdf = mock(Resource.class);
        when(dummyPdf.getFilename()).thenReturn("test.pdf");

        try (MockedConstruction<PagePdfDocumentReader> mockedReader = mockConstruction(PagePdfDocumentReader.class,
                (mock, context) -> {
                    when(mock.get()).thenReturn(List.of(new Document("Fake resume content")));
                })) {
            IngestionService service = new IngestionService(vectorStore, new Resource[]{dummyPdf});
            service.ingest();
            verify(vectorStore, atLeastOnce()).accept(anyList());
        }
    }
}
