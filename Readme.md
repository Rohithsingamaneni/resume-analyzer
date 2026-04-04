# Resume Intelligence Platform (Local RAG)

A privacy-first, enterprise-standard **Retrieval-Augmented Generation (RAG)** system built to analyze resumes locally. This project ensures that sensitive candidate data never leaves your infrastructure by leveraging local LLMs and vector databases.



---

## Overview
In 2026, data privacy is the top priority for HR tech. This platform solves the "Privacy vs. Intelligence" trade-off by running the entire AI pipeline—from document parsing to natural language reasoning—on local hardware in **Fremont, CA**.

### Core Capabilities
* **Private Embeddings:** Converts resume text into mathematical vectors using `nomic-embed-text`.
* **Semantic Search:** Uses **PGVector** to find candidates based on meaning, not just keywords.
* **Local Reasoning:** Powered by **Llama 3** via Ollama for human-like analysis.
* **Spring AI Integration:** Uses the latest Spring AI framework for a clean, scalable backend.

---

## Tech Stack
| Category | Technology |
| :--- | :--- |
| **Language** | Java 21 (LTS) |
| **Framework** | Spring Boot 3.4 / Spring AI |
| **Database** | PostgreSQL + PGVector |
| **AI Engine** | Ollama (Local) |
| **Build Tool** | Gradle |
| **Containerization** | Docker |

---

## Project Roadmap & Progress

### Phase 1: Foundation (Completed)
* [x] Set up Java 21 and Homebrew environment.
* [x] Initialized Gradle project with Spring AI dependencies.
* [x] Configured Dockerized PGVector instance.
* [x] Linked local Ollama models (`llama3`, `nomic-embed-text`).
* [x] Established secure Git/SSH workflow to GitHub.

### Phase 2: Ingestion Pipeline (Active)
* [ ] Implement PDF parsing with `PagePdfDocumentReader`.
* [ ] Set up `TokenTextSplitter` for optimal chunking.
* [ ] Develop the Vector Store ingestion service.
* [ ] Add metadata filtering (filename, upload date).



### Phase 3: Query & Analysis
* [ ] Build the Similarity Search retrieval service.
* [ ] Design the "Senior Recruiter" System Prompt.
* [ ] Implement real-time response streaming.

---

## Getting Started

### 1. Prerequisites
* **Java 21**
* **Docker Desktop**
* **Ollama**

### 2. Setup Infrastructure
```bash
# Start the Vector Database
docker-compose up -d

# Pull the AI models
ollama pull llama3
ollama pull nomic-embed-text