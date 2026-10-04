# Architecture notes

## Initial boundaries

- `api`: HTTP transport and request/response mapping.
- `domain`: pipeline, run, and job rules that do not depend on Spring or storage.
- `application`: use cases that coordinate domain operations and ports.
- `infrastructure`: database, source-control integrations, and queue adapters.
- `worker`: execution orchestration. Actual user commands belong in isolated worker processes/containers.

Create a package when there is code for it. Do not add empty package trees or make the domain depend on Spring, the database, or HTTP classes.

## Execution safety boundary

Pipeline files and commands are attacker-controlled input. Before executing a pipeline, define a separate worker boundary with an unprivileged identity, disposable workspace, CPU/memory/time/output limits, restricted network and filesystem access, cancellation, and explicit secret injection. The API process must never execute submitted commands directly.

## Persistence and scheduling

PostgreSQL is the durable source of truth. Flyway migrations own the schema; Hibernate validates the mapped schema at startup and does not mutate it. Pipeline definitions and their ordered steps are persisted together, and database constraints enforce case-insensitive name uniqueness and step ordering.

Pipeline creation accepts argument arrays, never a shell command string. The current API stores definitions only: it does not clone repositories or execute steps. Repository URLs are currently restricted to HTTPS. Authentication, outbound fetching, and execution require separate threat models and controls before implementation.

For future runs and jobs, treat a queue as a delivery mechanism, not the only record of work. Make job claiming and state transitions idempotent so a worker restart or duplicate delivery does not corrupt a run. Choose a queue only after the first scheduling behavior and deployment model are known.
