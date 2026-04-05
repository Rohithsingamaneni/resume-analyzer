package com.example.resume.model;

import java.util.List;

public record ResumeAnalysis(
        String candidateName,
        String highestEducation,
        int yearsOfExperience,
        List<String> topTechStack,
        String matchReasoning,
        String riskFactor,
        boolean isStrongCandidate
) {}
