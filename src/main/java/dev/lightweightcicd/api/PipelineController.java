package dev.lightweightcicd.api;

import dev.lightweightcicd.domain.PipelineDefinition;
import dev.lightweightcicd.service.PipelineService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pipelines")
public class PipelineController {
    private final PipelineService service;

    public PipelineController(PipelineService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PipelineDefinition> create(@Valid @RequestBody CreatePipelineRequest request) {
        PipelineDefinition pipeline = service.create(request.name(), request.repositoryUrl(), request.toSteps());
        return ResponseEntity.created(URI.create("/api/pipelines/" + pipeline.id()))
                .body(pipeline);
    }

    @GetMapping
    public List<PipelineDefinition> list() {
        return service.list();
    }
}
