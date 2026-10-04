CREATE TABLE pipelines (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    repository_url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX uq_pipelines_name ON pipelines (LOWER(name));

CREATE TABLE pipeline_steps (
    id UUID PRIMARY KEY,
    pipeline_id UUID NOT NULL REFERENCES pipelines (id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    position INTEGER NOT NULL,
    command_arguments JSONB NOT NULL,
    CONSTRAINT uq_pipeline_step_position UNIQUE (pipeline_id, position),
    CONSTRAINT uq_pipeline_step_name UNIQUE (pipeline_id, name)
);

CREATE INDEX ix_pipeline_steps_pipeline_id ON pipeline_steps (pipeline_id);
