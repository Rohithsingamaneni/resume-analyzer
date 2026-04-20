# Resume Intelligence Platform (Local RAG)

A privacy-first, enterprise-standard **Retrieval-Augmented Generation (RAG)** system built to analyze resumes locally. Designed for the high-compliance needs of 2026, this platform ensures sensitive candidate data never leaves your infrastructure by leveraging local LLMs and vector databases.

---

## Quick Start Guide

To run the application locally, follow these steps to ensure all databases and ML models are active:

### 1. Start the Database (PGVector)
The application requires PostgreSQL with the `pgvector` extension.
```bash
docker compose up -d
```

### 2. Start the AI Engine (Ollama)
The application relies on local Offline LLMs for privacy. You must have [Ollama](https://ollama.com/) running on your machine.
```bash
# Start the Ollama application, then pull the required models:
ollama pull llama3.1:8b            # Used for resume analysis and chat
ollama pull nomic-embed-text:latest # Used for generating vector embeddings
```

### 3. Generate the ONNX Model
The project uses a custom ONNX machine learning model for ranking candidates.
```bash
cd ml
# Ensure you have the required python dependencies (e.g., numpy, onnx, torch, scikit-learn)
python3 generate_model.py
```
*(Note: The model is strictly exported using Opset 19 to ensure compatibility with the Java `onnxruntime` dependency).*

### 4. Run the Spring Boot Application
With the database, Ollama, and ONNX model ready, start the backend:
```bash
./gradlew bootRun
```

---

## API Endpoints & Testing

Once the application is running on `http://localhost:8080`, you can test the following features:

### 1. Ingest Resumes
Load and embed the PDFs from `src/main/resources/resumes/` into your PGVector database.
```bash
curl -X POST http://localhost:8080/api/resumes/ingest
```

### 2. Screen Candidate (Structured Extraction)
Analyze the ingested resumes against a specific job role and return a structured JSON evaluation.
```bash
curl "http://localhost:8080/api/query/screen?role=Software+Engineer"
```

### 3. Ask a General Question
Ask the AI assistant a direct question about the ingested candidate pool.
```bash
curl "http://localhost:8080/api/query/analyze?question=What+are+the+candidate's+core+strengths"
```

---

## Recent Architecture Wins
* **Local Machine Learning Integration:** Successfully bridged Python-based ONNX model generation with Java's `onnxruntime`, ensuring seamless cross-environment compatibility.
* **Structured Data Extraction:** Implemented `BeanOutputConverter` to transform unstructured PDF text into deterministic Java Records.
* **Resilient CI/CD Testing:** Established a dual-layer testing strategy using **Testcontainers** (PGVector) for integration and **Mockito Deep Stubs** for isolated service verification. 

---

## Tech Stack
| Category | Technology |
| :--- | :--- |
| **Language** | Java 21 (LTS) & Python 3 |
| **Framework** | Spring Boot 3.4.1 / Spring AI 1.0.0-M5 |
| **Database** | PostgreSQL 16 + PGVector (Dockerized) |
| **AI Engine** | Ollama (Llama 3.1 & Nomic-Embed-Text) |
| **ML Runtime** | ONNX Runtime 1.19.2 |
| **Build Tool** | Gradle 9.x |
| **Testing** | JUnit 5, Mockito, Testcontainers |

---

## Project Roadmap & Progress

### Phase 1: Foundation (Completed) 
* [x] Initialized Java 21 / Spring AI environment.
* [x] Configured Dockerized PGVector with automated schema initialization.
* [x] Established local Ollama model orchestration.

### Phase 2: Ingestion Pipeline (Completed) 
* [x] Implemented PDF parsing via `PagePdfDocumentReader`.
* [x] Tuned `TokenTextSplitter` for optimal chunking.
* [x] Developed `IngestionService` with defensive resource injection for CI/CD stability.

### Phase 3: Query & Analysis (Completed) 
* [x] Built Retrieval logic using modern `SearchRequest` builders.
* [x] Implemented **Structured Entity Extraction** to return JSON-mapped `ResumeAnalysis` records.
* [x] Resolved `-parameters` reflection issues in Spring 6.1+ for `@RequestParam`.

### Phase 4: Advanced Intelligence (Active) 
* [ ] **Ranking Evaluator:** Implementing a "Match Score" logic to rank candidates using the ONNX model.
* [ ] **Hybrid Search:** Combining Vector similarity with Postgres Full-Text Search.
* [ ] **Multi-Resume Comparison:** Batch analysis for competitive candidate ranking.