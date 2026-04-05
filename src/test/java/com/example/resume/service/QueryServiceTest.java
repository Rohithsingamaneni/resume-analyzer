package com.example.resume.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryServiceTest {

    @Mock
    private VectorStore vectorStore;

    @Mock
    private ChatClient.Builder chatClientBuilder;

    private QueryService queryService;

    @BeforeEach
    void setUp() {
        ChatClient deepStubClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);

        when(chatClientBuilder.build()).thenReturn(deepStubClient);

        queryService = new QueryService(chatClientBuilder, vectorStore);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAnalyze_ShouldReturnAiResponse() {
        Document mockDoc = new Document("Experience: Apple Maps and gRPC.");
        when(vectorStore.similaritySearch(any(SearchRequest.class)))
                .thenReturn(List.of(mockDoc));

        ChatClient chatClient = chatClientBuilder.build();
        when(chatClient.prompt()
                .user(any(Consumer.class))
                .call()
                .content())
                .thenReturn("Rohith is a gRPC expert.");

        String response = queryService.analyze("Tell me about Rohith's experience.");

        assertThat(response).isEqualTo("Rohith is a gRPC expert.");

        verify(vectorStore, times(1)).similaritySearch(any(SearchRequest.class));
    }
}
