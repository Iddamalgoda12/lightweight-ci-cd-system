package dev.lightweightcicd.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pipelines")
public class PipelineEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "repository_url", nullable = false, length = 2048)
    private String repositoryUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(nullable = false)
    private long version;

    @OneToMany(mappedBy = "pipeline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PipelineStepEntity> steps = new ArrayList<>();

    protected PipelineEntity() {}

    public PipelineEntity(UUID id, String name, String repositoryUrl, Instant now) {
        this.id = id;
        this.name = name;
        this.repositoryUrl = repositoryUrl;
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void addStep(PipelineStepEntity step) {
        steps.add(step);
        step.setPipeline(this);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public List<PipelineStepEntity> getSteps() {
        return steps;
    }
}
