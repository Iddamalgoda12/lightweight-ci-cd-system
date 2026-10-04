package dev.lightweightcicd.api;

import dev.lightweightcicd.domain.PipelineStep;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreatePipelineRequest(
        @NotBlank @Size(max = 100) String name,

        @NotBlank @Size(max = 2048) @Pattern(regexp = "https://.+", message = "must be an HTTPS URL")
        String repositoryUrl,

        @NotEmpty @Size(max = 50) List<@Valid StepRequest> steps) {

    public List<PipelineStep> toSteps() {
        return steps.stream()
                .map(step -> new PipelineStep(step.name(), step.commandArguments()))
                .toList();
    }

    public record StepRequest(
            @NotBlank @Size(max = 100) String name,
            @NotEmpty @Size(max = 64) List<@NotBlank @Size(max = 2048) String> commandArguments) {}
}
