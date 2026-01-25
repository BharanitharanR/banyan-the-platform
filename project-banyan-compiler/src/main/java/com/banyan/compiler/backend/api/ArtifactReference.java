package com.banyan.compiler.backend.api;

import com.banyan.compiler.enums.ArtifactType;

public record ArtifactReference(
        ArtifactType type,
        String id,
        int version
) {}
