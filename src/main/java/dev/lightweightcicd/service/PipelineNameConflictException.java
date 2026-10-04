package dev.lightweightcicd.service;

public class PipelineNameConflictException extends RuntimeException {
    public PipelineNameConflictException(String name, Throwable cause) {
        super("A pipeline named '%s' already exists".formatted(name), cause);
    }
}
