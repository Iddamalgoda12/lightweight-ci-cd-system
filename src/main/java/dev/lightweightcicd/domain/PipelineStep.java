package dev.lightweightcicd.domain;

import java.util.List;

public record PipelineStep(String name, List<String> commandArguments) {
    public PipelineStep {
        commandArguments = List.copyOf(commandArguments);
    }
}
