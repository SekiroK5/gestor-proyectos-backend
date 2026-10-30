# Gestor de Proyectos - Backend (API REST)

API REST del Gestor de Proyectos (Spring Boot 3.5, **Java 21**, PostgreSQL 16).

## Ejecutar

```bash
./mvnw spring-boot:run
```

La API queda en `http://localhost:8081`. La conexion a la base se configura con `DB_URL`, `DB_USER` y `DB_PASSWORD`.

## Pruebas y cobertura

```bash
./mvnw verify
```

Reporte JaCoCo en `target/site/jacoco/index.html`.

## Integracion continua (Docker)

- `ci/docker-compose.yml`: Jenkins con JDK 21 (`ci/jenkins/Dockerfile`) y la base `gestor-db`.
- `Jenkinsfile`: pipeline del backend (compilacion, pruebas, JaCoCo, SonarQube, Trivy).
- `database/Jenkinsfile`: pipeline de base de datos (sqlfluff, esquema, verificacion de integridad, validacion contra JPA).
- `docs/pom/`: POM original y POM actualizado.
