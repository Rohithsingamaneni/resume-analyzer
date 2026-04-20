package com.example.resume.service;

import ai.onnxruntime.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

@Service
public class RankingService implements AutoCloseable {

    private final OrtEnvironment env;
    private final OrtSession session;

    public RankingService() throws OrtException, IOException {
        this.env = OrtEnvironment.getEnvironment();
        byte[] modelArray = new ClassPathResource("models/resume_ranker.onnx").getContentAsByteArray();
        this.session = env.createSession(modelArray, new OrtSession.SessionOptions());
    }

    public float calculateMatchScore(float[] candidateFeatures) throws OrtException {
        // Create a tensor from our features (e.g., years of exp, skill counts, etc.)
        try (OnnxTensor testTensor = OnnxTensor.createTensor(env, new float[][]{candidateFeatures})) {
            // Run inference
            try (OrtSession.Result results = session.run(Collections.singletonMap("X", testTensor))) {
                // Return the probability/score from the model
                float[][] score = (float[][]) results.get(0).getValue();
                return score[0][0] * 100; // Convert to 0-100 scale
            }
        }
    }

    @Override
    public void close() throws OrtException {
        session.close();
        env.close();
    }
}
