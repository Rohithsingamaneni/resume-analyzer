package com.example.resume.service;

import com.example.resume.model.ResumeAnalysis;

import org.springframework.lang.NonNull;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QueryService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public QueryService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    /**
     * Structured Analysis for Recruiters
     */
    @SuppressWarnings("null")
    public ResumeAnalysis getStructuredAnalysis(@NonNull String query) {
        var converter = new BeanOutputConverter<>(ResumeAnalysis.class);
        String context = getContextFromVectorStore(query);

        String promptTemplate = """
                Analyze the candidate based on the provided resume context.
                Focus on technical skills and experience levels.
                
                {format}
                
                CONTEXT:
                {context}
                
                USER_QUERY:
                {query}
                """;

        Map<String, Object> params = Map.of(
                "context", context,
                "query", query,
                "format", converter.getFormat() // Injects the JSON schema
        );

        return chatClient.prompt()
                .user(u -> u.text(promptTemplate)
                        .params(params))
                .call()
                .entity(ResumeAnalysis.class); // Automatically converts JSON -> ResumeAnalysis record
    }

    @SuppressWarnings("null")
    public String analyze(@NonNull String query) {
        String context = getContextFromVectorStore(query);
        String promptTemplate = """
                You are a recruiter assistant. Answer based ONLY on the context.
                CONTEXT: {context}
                QUESTION: {question}
                """;

        Map<String, Object> params = Map.of("context", context, "question", query);

        return chatClient.prompt()
                .user(u -> u.text(promptTemplate)
                        .params(params))
                .call()
                .content();
    }

    private String getContextFromVectorStore(@NonNull String query) {
        return Objects.requireNonNull(vectorStore.similaritySearch(
                        SearchRequest.builder()
                                .query(query)
                                .topK(4)
                                .build()))
                .stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
