package com.banyan.compiler.core;

import java.util.List;

public class CompilationResult {
    private final boolean success;

    private final List<String> errors;
    private final List<String> warnings;
    private final Object artifact;

    private CompilationResult(
            boolean success,
            List<String> errors,
            List<String> warnings,
            Object artifact
    ) {
        this.success = success;
        this.errors = errors;
        this.warnings = warnings;
        this.artifact = artifact;
    }

    public static CompilationResult failure(List<String> errors) {
        return new CompilationResult(false, errors, List.of(), null);
    }

    public static CompilationResult success(Object artifact, List<String> warnings) {
        return new CompilationResult(true, List.of(), warnings, artifact);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public Object getArtifact() {
        return artifact;
    }
}
