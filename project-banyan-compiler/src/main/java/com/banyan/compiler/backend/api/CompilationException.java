package com.banyan.compiler.backend.api;

import com.banyan.compiler.enums.ArtifactType;

public class CompilationException extends RuntimeException {

    private final CompilationErrorCode errorCode;
    private final ArtifactType artifactType;
    private final String artifactId;
    private final Integer artifactVersion;

    public CompilationException(
            CompilationErrorCode errorCode,
            String message
    ) {
        super(message);
        this.errorCode = errorCode;
        this.artifactType = null;
        this.artifactId = null;
        this.artifactVersion = null;
    }

    public CompilationException(
            CompilationErrorCode errorCode,
            ArtifactType artifactType,
            String artifactId,
            Integer artifactVersion,
            String message
    ) {
        super(message);
        this.errorCode = errorCode;
        this.artifactType = artifactType;
        this.artifactId = artifactId;
        this.artifactVersion = artifactVersion;
    }

    public CompilationException(
            CompilationErrorCode errorCode,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.errorCode = errorCode;
        this.artifactType = null;
        this.artifactId = null;
        this.artifactVersion = null;
    }

    public CompilationErrorCode getErrorCode() {
        return errorCode;
    }

    public ArtifactType getArtifactType() {
        return artifactType;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public Integer getArtifactVersion() {
        return artifactVersion;
    }
}
