package com.banyan.compiler.backend.api;

import com.banyan.compiler.enums.ArtifactType;

public interface CompiledArtifact {
    String id();
    ArtifactType type();
    int version();

}
