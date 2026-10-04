package dev.lightweightcicd.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "pipeline_steps")
public class PipelineStepEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pipeline_id", nullable = false)
    private PipelineEntity pipeline;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int position;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "command_arguments", nullable = false, columnDefinition = "jsonb")
    private List<String> commandArguments;

    protected PipelineStepEntity() {}

    public PipelineStepEntity(UUID id, String name, int position, List<String> commandArguments) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.commandArguments = commandArguments;
    }

    void setPipeline(PipelineEntity pipeline) {
        this.pipeline = pipeline;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public List<String> getCommandArguments() {
        return commandArguments;
    }
}
