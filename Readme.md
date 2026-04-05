# Resume Intelligence Platform (Local RAG)

A privacy-first, enterprise-standard **Retrieval-Augmented Generation (RAG)** system built to analyze resumes locally. Designed for the high-compliance needs of 2026, this platform ensures sensitive candidate data never leaves your infrastructure by leveraging local LLMs and vector databases.

---

##  Recent Architecture Wins
* **Structured Data Extraction:** Successfully implemented `BeanOutputConverter` to transform unstructured PDF text into deterministic Java Records.
* **Fluent Request Builder:** Developed a custom DSL to decouple API contracts from AI orchestration, allowing for expressive, type-safe queries.
* **Resilient CI/CD Testing:** Established a dual-layer testing strategy using **Testcontainers** (PGVector) for integration and **Mockito Deep Stubs** for isolated service verification.

---

##  Tech Stack
| Category | Technology |
| :--- | :--- |
| **Language** | Java 21 (LTS) |
| **Framework** | Spring Boot 3.4.1 / Spring AI 1.0.0-M5 |
| **Database** | PostgreSQL 16 + PGVector (Dockerized) |
| **AI Engine** | Ollama (Llama 3.1 & Nomic-Embed-Text) |
| **Build Tool** | Gradle 9.x |
| **Testing** | JUnit 5, Mockito, Testcontainers |

---

##  Project Roadmap & Progress

### Phase 1: Foundation (Completed) 
* [x] Initialized Java 21 / Spring AI environment.
* [x] Configured Dockerized PGVector with automated schema initialization.
* [x] Established local Ollama model orchestration.

### Phase 2: Ingestion Pipeline (Completed) 
* [x] Implemented PDF parsing via `PagePdfDocumentReader`.
* [x] Tuned `TokenTextSplitter` for optimal chunking (800 token chunks / 100 token overlap).
* [x] Developed `IngestionService` with defensive resource injection for CI/CD stability.
* [x] Verified semantic ingestion with Integration Tests.

### Phase 3: Query & Analysis (Completed) 
* [x] Built Retrieval logic using modern `SearchRequest` builders.
* [x] Implemented **Structured Entity Extraction** to return JSON-mapped `ResumeAnalysis` records.
* [x] Designed a **Fluent Request Builder** for human-readable query logic.
* [x] Achieved 100% unit test coverage for service orchestration.

### Phase 4: Advanced Intelligence (Active) 
* [ ] **Hybrid Search:** Combining Vector similarity with Postgres Full-Text Search (TSVECTOR).
* [ ] **Ranking Evaluator:** Implementing a "Match Score" logic to rank candidates 0-100.
* [ ] **Multi-Resume Comparison:** Batch analysis for competitive candidate ranking.

---

##  Feature Highlight: Fluent Request Builder
The platform utilizes a custom-built DSL to make complex resume analysis readable and maintainable:

```java
// Example of the Custom Builder in action
return queryService.newRequest()
        .forRole("Senior Backend Engineer")
        .withExperience(5)
        .requiring("Java", "gRPC", "Vector DB")
        .analyze(); // Returns a structured ResumeAnalysis object