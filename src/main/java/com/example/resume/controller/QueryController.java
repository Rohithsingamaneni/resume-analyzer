package com.example.resume.controller;

import com.example.resume.model.ResumeAnalysis;
import com.example.resume.service.QueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/analyze")
    public String analyzeResume(@RequestParam(defaultValue = "What are the candidate's core strengths?") String question) {
        return queryService.analyze(question);
    }

    @GetMapping("/screen")
    public ResumeAnalysis screenCandidate(@RequestParam(defaultValue = "Software Engineer") String role) {
        return queryService.getStructuredAnalysis("Analyze this resume for a " + role + " position.");
    }
}
