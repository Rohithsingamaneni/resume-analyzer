pipeline {
    agent any // Or 'docker' if you want Jenkins to spin up a build container

    parameters {
        booleanParam(name: 'RUN_HEAVY_TESTS', defaultValue: false, description: 'Run LLM-based integration tests?')
    }

    environment {
        // Essential for Spring Boot to find your local services
        SPRING_PROFILES_ACTIVE = 'ci'
        DB_URL = "jdbc:postgresql://localhost:5432/vector_db"
        OLLAMA_HOST = "http://localhost:11434"
    }

    stages {
        stage('🚀 Prepare') {
            steps {
                echo "Starting build for Rohith's Resume Analyzer..."
                sh 'java -version'
                sh './gradlew --version'
            }
        }

        stage('🔨 Compile') {
            steps {
                // Clean the build to avoid "ghost" errors
                sh './gradlew clean compileJava'
            }
        }

        stage('🧪 Regression Tests') {
            steps {
                // Fast unit tests only
                sh './gradlew test --tests "*UnitTest"'
            }
        }

        stage('🧬 Integration Tests') {
            when {
                // Only run this if the DB is actually up
                expression { return params.RUN_HEAVY_TESTS }
            }
            steps {
                // This is where Testcontainers spins up PGVector
                sh './gradlew test --tests "*IntegrationTest"'
            }
        }

        stage('📦 Artifact Creation') {
            steps {
                // Creates the executable .jar file
                sh './gradlew bootJar'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }

        stage('Unit Tests') {
            steps {
                // Run everything EXCEPT the Integration tests
                sh './gradlew test --tests "*ServiceTest"'
            }
        }
    }

    post {
        success {
            echo "✅ Build Successful! Check the Artifacts tab for your .jar file."
        }
        failure {
            echo "❌ Build Failed. Check the 'Console Output' to see what Rohith broke."
        }
    }
}
