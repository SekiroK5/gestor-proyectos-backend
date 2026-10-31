// Pipeline del backend (API REST): compila con Java 21, ejecuta pruebas con cobertura JaCoCo,
// analiza el codigo en SonarQube y busca vulnerabilidades con Trivy sin revisar las carpetas target.
pipeline {
    agent any

    tools {
        jdk 'JDK 21'
        maven 'Maven 3.9.11'
    }

    environment {
        // Base de datos del contenedor gestor-db (red jenkins-ci) para la prueba de contexto.
        DB_URL = 'jdbc:postgresql://gestor-db:5432/dbgestor'
        DB_USER = 'postgres'
        DB_PASSWORD = 'postgres'
        TRIVY_CACHE_DIR = '/var/jenkins_home/.cache/trivy'
        // Trivy lee las dependencias del repositorio local de Maven (~/.m2) en lugar de pedirlas a Maven Central.
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '15'))
    }

    stages {
        stage('Entorno') {
            steps {
                sh 'java -version'
                sh 'mvn -v'
            }
        }

        stage('Compilar') {
            steps {
                sh 'mvn -B -ntp clean compile'
            }
        }

        stage('Pruebas y cobertura') {
            steps {
                sh 'mvn -B -ntp verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    recordCoverage(tools: [[parser: 'JACOCO', pattern: 'target/site/jacoco/jacoco.xml']],
                                   sourceDirectories: [[path: 'src/main/java']])
                    archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
                }
            }
        }

        stage('Analisis SonarQube') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn -B -ntp sonar:sonar'
                }
            }
        }

        stage('Escaneo de vulnerabilidades') {
            steps {
                sh 'trivy fs --offline-scan --skip-version-check --scanners vuln,secret,misconfig --skip-dirs target --skip-dirs "**/target" --format table --output trivy-backend.txt .'
                sh 'trivy fs --offline-scan --skip-version-check --scanners vuln,secret,misconfig --skip-dirs target --skip-dirs "**/target" --format json --output trivy-backend.json .'
                sh 'cat trivy-backend.txt'
                script {
                    def graves = sh(returnStatus: true,
                        script: 'trivy fs --offline-scan --skip-version-check --scanners vuln,secret --skip-dirs target --skip-dirs "**/target" --severity HIGH,CRITICAL --exit-code 1 --quiet .')
                    if (graves != 0) {
                        unstable('Trivy encontro vulnerabilidades HIGH o CRITICAL, revisar trivy-backend.txt')
                    }
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'trivy-backend.*', allowEmptyArchive: true
                }
            }
        }

        stage('Empaquetar') {
            steps {
                sh 'mvn -B -ntp -DskipTests package'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
}
