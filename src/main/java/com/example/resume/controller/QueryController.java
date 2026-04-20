package com.example.resume.controller;

import com.example.resume.model.ResumeAnalysis;
import com.example.resume.service.QueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.lang.NonNull;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/analyze")
    public String analyzeResume(@RequestParam(name = "question", defaultValue = "What are the candidate's core strengths?") @NonNull String question) {
        return queryService.analyze(question);
    }

    @GetMapping("/screen")
    public ResumeAnalysis screenCandidate(@RequestParam(name = "role", defaultValue = "Software Engineer") @NonNull String role) {
        return queryService.getStructuredAnalysis("Analyze this resume for a " + role + " position.");
    }
}
