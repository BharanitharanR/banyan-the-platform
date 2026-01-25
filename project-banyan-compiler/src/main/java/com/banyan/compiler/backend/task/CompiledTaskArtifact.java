package com.banyan.compiler.backend.task;

import com.banyan.compiler.backend.api.AbstractCompiledArtifact;
import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.enums.ArtifactType;

import java.util.List;

public class CompiledTaskArtifact extends AbstractCompiledArtifact<CompiledTask> {

    public CompiledTaskArtifact(String id, int version, CompiledTask field, CompilationMetadata metadata, List<ArtifactReference> dependencies) {
        super(id,version,ArtifactType.Task,field,metadata,dependencies);
    }

}
