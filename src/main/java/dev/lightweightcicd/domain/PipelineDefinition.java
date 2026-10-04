package dev.lightweightcicd.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PipelineDefinition(
        UUID id, String name, String repositoryUrl, List<PipelineStep> steps, Instant createdAt, Instant updatedAt) {
    public PipelineDefinition {
        steps = List.copyOf(steps);
    }
}
