package dev.lightweightcicd.service;

import dev.lightweightcicd.domain.PipelineDefinition;
import dev.lightweightcicd.domain.PipelineStep;
import dev.lightweightcicd.infrastructure.persistence.PipelineEntity;
import dev.lightweightcicd.infrastructure.persistence.PipelineRepository;
import dev.lightweightcicd.infrastructure.persistence.PipelineStepEntity;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PipelineService {
    private final PipelineRepository repository;

    public PipelineService(PipelineRepository repository) {
        this.repository = repository;
    }

    public PipelineDefinition create(String name, String repositoryUrl, List<PipelineStep> steps) {
        Instant now = Instant.now();
        PipelineEntity pipeline = new PipelineEntity(UUID.randomUUID(), name, repositoryUrl, now);
        for (int i = 0; i < steps.size(); i++) {
            PipelineStep step = steps.get(i);
            pipeline.addStep(new PipelineStepEntity(UUID.randomUUID(), step.name(), i, step.commandArguments()));
        }
        try {
            return toDefinition(repository.saveAndFlush(pipeline));
        } catch (DataIntegrityViolationException exception) {
            throw new PipelineNameConflictException(name, exception);
        }
    }

    @Transactional(readOnly = true)
    public List<PipelineDefinition> list() {
        return repository.findAll().stream().map(PipelineService::toDefinition).toList();
    }

    private static PipelineDefinition toDefinition(PipelineEntity entity) {
        List<PipelineStep> steps = entity.getSteps().stream()
                .sorted(java.util.Comparator.comparingInt(PipelineStepEntity::getPosition))
                .map(step -> new PipelineStep(step.getName(), step.getCommandArguments()))
                .toList();
        return new PipelineDefinition(
                entity.getId(),
                entity.getName(),
                entity.getRepositoryUrl(),
                steps,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
