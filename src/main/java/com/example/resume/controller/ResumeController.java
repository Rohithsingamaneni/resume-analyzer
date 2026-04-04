package com.example.resume.controller;
import com.example.resume.service.IngestionService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {
    private final ChatClient chatClient;
    private final IngestionService ingestionService;

    public ResumeController(ChatClient chatClient, IngestionService ingestionService) {
        this.chatClient = chatClient;
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest")
    public String ingest() {
        ingestionService.ingest();
        return "Resumes indexed!";
    }

    @GetMapping("/analyze")
    public String analyze(@RequestParam String query) {
        return chatClient.prompt().user(query).call().content();
    }
}
