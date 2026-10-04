# Lightweight CI/CD

This repository currently contains an experimental C++ runner and a Java implementation in `java/`. The Java service is being built incrementally, starting with its application skeleton and health endpoints.

See [`java/README.md`](java/README.md) for Fedora prerequisites and run instructions.
# Lightweight CI/CD

A small, self-hosted CI/CD service written in Java. The project is being built incrementally, with safe pipeline execution and worker isolation treated as core design requirements.

## Stack

- Java 25
- Spring Boot 4.1.1
- Maven 3.9.11 via Maven Wrapper
- JUnit 5 and Spring Boot Test

## Fedora development setup

Install the JDK and Git. Maven is optional because the checked-in wrapper provides the project's pinned Maven version.

```bash
sudo dnf install java-25-openjdk-devel git
java -version
```

If your enabled Fedora repositories do not provide JDK 25, install a JDK supported by Spring Boot 4 and change `java.version` to match.

## Build, test, and run

```bash
./mvnw verify
docker compose up -d postgres
./mvnw spring-boot:run
```

The API listens on port 8080 by default. Set `PORT` to change it. PostgreSQL connection settings can be overridden with `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD`. The compose password default is for local development only; set `DATABASE_PASSWORD` to a private value before starting the database.

## Pipeline definitions

Create and list declarative pipeline definitions at `/api/pipelines`. Definitions are stored in PostgreSQL and schema changes are applied through Flyway. A definition contains an HTTPS repository URL and up to 50 ordered steps. Each step has a name and an argument array; these values are stored as configuration and are not executed by the API.

```bash
curl -i http://localhost:8080/api/pipelines \
  -H 'Content-Type: application/json' \
  -d '{"name":"example","repositoryUrl":"https://github.com/example/project.git","steps":[{"name":"build","commandArguments":["./mvnw","--batch-mode","package"]}]}'
```

Pipeline names are unique regardless of letter case. Invalid requests return `400` with a problem response; duplicate names return `409`.

## Project layout

```text
.
├── .github/workflows/     # CI automation
├── .mvn/wrapper/          # Maven Wrapper version/configuration
├── docs/                  # Architecture and development notes
├── src/
│   ├── main/
│   │   ├── java/dev/lightweightcicd/
│   │   │   ├── api/       # HTTP boundary
│   │   │   ├── config/    # Application configuration
│   │   │   ├── domain/    # Pipeline definition concepts
│   │   │   ├── service/   # Pipeline use cases
│   │   │   └── infrastructure/persistence/ # PostgreSQL adapters
│   │   └── resources/db/migration/ # Flyway schema
│   └── test/java/         # Tests mirror production packages
├── pom.xml
└── mvnw
```

Pipeline commands are untrusted code; they must not run inside the API process. Worker isolation, timeouts, output limits, and secret handling will be designed before execution is enabled.
# Lightweight CI/CD

A small, self-hosted CI/CD service written in Java. The project is being built incrementally, with safe pipeline execution and worker isolation treated as core design requirements.

## Stack

- Java 25
- Spring Boot 4.1.1
- Maven 3.9.11 via Maven Wrapper
- JUnit 5 and Spring Boot Test

## Fedora development setup

Install the JDK and Git. Maven is optional because the checked-in wrapper provides the project's pinned Maven version.

```bash
sudo dnf install java-25-openjdk-devel git
java -version
```

If your enabled Fedora repositories do not provide JDK 25, install a JDK supported by Spring Boot 4 and change `java.version` to match.

## Build, test, and run

```bash
./mvnw verify
./mvnw spring-boot:run
```

The API listens on port 8080 by default. Set `PORT` to change it.

## Project layout

```text
.
├── .github/workflows/     # CI automation
├── .mvn/wrapper/          # Maven Wrapper version/configuration
├── docs/                  # Architecture and development notes
├── src/
│   ├── main/
│   │   ├── java/dev/lightweightcicd/
│   │   │   ├── api/       # HTTP boundary
│   │   │   ├── config/    # Application configuration
│   │   │   ├── domain/    # Core pipeline/job concepts
│   │   │   ├── service/   # Use cases and orchestration
│   │   │   └── worker/    # Isolated execution boundary
│   │   └── resources/
│   └── test/java/         # Tests mirror production packages
├── pom.xml
└── mvnw
```

We will add package areas as needed instead of creating empty placeholders. Pipeline commands are untrusted code; they must not run inside the API process. Worker isolation, timeouts, output limits, and secret handling will be designed before execution is enabled.
# Lightweight CI/CD

A small, self-hosted continuous integration and delivery service, built in Java. The goal is to learn and apply production practices while keeping the first version understandable and compact.

## Technology choices

- Java 25, matching the JDK installed in the development environment.
- Spring Boot 4.1.1 for the HTTP API, configuration, operational health endpoints, and a supported integration path for persistence and background work.
- Maven Wrapper pinned to Maven 3.9.16 so local and CI builds use the same Maven release.
- JUnit 5 and Spring Boot Test for automated tests.

Spring Boot is useful here because this service is an always-on server, rather than a command-line-only runner. It supplies the HTTP and operational foundations. We will add database, queue, and worker dependencies only when implementing those parts.

## Fedora setup

Install a JDK and basic development tools:

```bash
sudo dnf install java-25-openjdk-devel git curl unzip
java -version
```

Maven does not need to be installed system-wide. Use the project wrapper for builds.

## Build and run

```bash
./mvnw verify
./mvnw spring-boot:run
```

The service listens on port 8080. Override it with `PORT`, for example `PORT=9090 ./mvnw spring-boot:run`. Health is available at `/actuator/health`; the initial API information endpoint is `/api`.

## Repository layout

```text
.
├── .github/
│   ├── dependabot.yml       # Dependency update proposals
│   └── workflows/ci.yml     # Pull request and main branch verification
├── .mvn/wrapper/            # Pinned Maven Wrapper configuration
├── docs/                    # Architecture and implementation decisions
├── src/
│   ├── main/java/dev/lightweightcicd/
│   │   └── api/             # HTTP interface
│   ├── main/resources/      # Runtime configuration
│   └── test/java/           # Tests mirror production packages
├── pom.xml
└── mvnw
```

Packages should be introduced when a feature needs them. The initial architecture keeps the HTTP layer separate from the pipeline domain and execution adapter. Pipeline commands are untrusted code: they must eventually run in isolated workers with resource limits, timeouts, and explicit secret handling, never inside the API process.

## Current status

The current increment is only the application shell and service health/API endpoints. Pipeline definitions, storage, queueing, authentication, and workers are not implemented yet.
