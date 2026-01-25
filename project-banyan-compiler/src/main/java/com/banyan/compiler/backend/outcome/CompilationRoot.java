package com.banyan.compiler.backend.outcome;

import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.enums.ArtifactType;

public record CompilationRoot(
        ArtifactType type,
        String id,
        int version
) {
    public ArtifactReference[] dependencies() {
        return new ArtifactReference[0];
    }
}

