package com.example.resume.controller;

import com.example.resume.service.IngestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
@Slf4j
public class ResumeController {

    private final IngestionService ingestionService;
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ResumeController(IngestionService ingestionService, ChatClient.Builder builder, VectorStore vectorStore) {
        this.ingestionService = ingestionService;
        this.vectorStore = vectorStore;
        this.chatClient = builder.build();
    }

    @PostMapping("/ingest")
    public String ingest() {
        ingestionService.ingest();
        return "Ingestion complete! Resumes are now searchable in the database.";
    }

    @GetMapping("/analyze")
    public String analyze(@RequestParam String query) {
        log.info("Analyzing resumes with query: {}", query);
        return chatClient.prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .user(query)
                .call()
                .content();
    }
}
