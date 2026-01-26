package com.banyan.compiler.backend.api;

import com.banyan.compiler.enums.ArtifactType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
@JsonSerialize(using=CompiledArtifactSerializer.class)
public abstract class AbstractCompiledArtifact<T>
        implements CompiledArtifact<T> {

    private final String id;
    private final int version;
    private final ArtifactType type;
    private final T payload;
    private final CompilationMetadata metadata;
    private final List<ArtifactReference> dependencies;

    protected AbstractCompiledArtifact(
            String id,
            int version,
            ArtifactType type,
            T payload,
            CompilationMetadata metadata,
            List<ArtifactReference> dependencies
    ) {
        this.id = id;
        this.version = version;
        this.type = type;
        this.payload = payload;
        this.metadata = metadata;
        this.dependencies = dependencies;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public int version() {
        return version;
    }

    @Override
    public ArtifactType type() {
        return type;
    }

    @Override
    public CompilationMetadata metadata() {
        return metadata;
    }

    @Override
    public T payload() {
        return payload;
    }

    @Override
    public List<ArtifactReference> dependencies()
    {
        return this.dependencies;
    }

}
