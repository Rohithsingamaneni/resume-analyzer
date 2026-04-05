package com.example.resume.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QueryService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public QueryService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    public String analyze(String query) {
        // 1. Updated Retrieval: Using the modern Builder pattern
        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(4) // This replaces the deprecated withTopK
                        .build()
        );

        // 2. Extract content from documents
        String content = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        // 3. Create the RAG Prompt Template
        String promptTemplate = """
                You are a professional recruiter assistant. 
                Use the following pieces of retrieved context from a candidate's resume to answer the question.
                If the answer isn't in the context, say you don't know. 
                
                CONTEXT:
                {context}
                
                QUESTION:
                {question}
                """;

        // 4. Generation: Using .params() to inject variables into the template
        return chatClient.prompt()
                .user(u -> u.text(promptTemplate)
                        .params(Map.of("context", content, "question", query)))
                .call()
                .content();
    }
}
