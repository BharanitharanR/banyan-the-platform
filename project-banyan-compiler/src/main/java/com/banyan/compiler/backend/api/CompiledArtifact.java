package com.banyan.compiler.backend.api;

import com.banyan.compiler.enums.ArtifactType;

import java.util.List;

public interface CompiledArtifact<T> {
    String id();
    ArtifactType type();
    int version();
    CompilationMetadata metadata();
    T payload();
    default List<ArtifactReference> dependencies() {
        return List.of();
    }
}
