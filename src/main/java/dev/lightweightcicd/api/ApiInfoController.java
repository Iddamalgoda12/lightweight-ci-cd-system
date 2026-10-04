package dev.lightweightcicd.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiInfoController {
    @GetMapping("/api")
    public ApiInfo info() {
        return new ApiInfo("Lightweight CI/CD", "running");
    }

    public record ApiInfo(String name, String status) {}
}
