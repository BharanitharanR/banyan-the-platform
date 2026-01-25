package com.banyan.compiler.backend.evidence;

import com.banyan.compiler.backend.api.AbstractCompiledArtifact;
import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.enums.ArtifactType;


import java.util.List;
import java.util.Map;

public final class CompiledEvidenceTypeArtifact extends AbstractCompiledArtifact<CompiledEvidenceType> {

    public CompiledEvidenceTypeArtifact(String id, int version, CompiledEvidenceType fields, CompilationMetadata metadata, List<ArtifactReference> dependencies) {
        super(id,version, ArtifactType.EvidenceType,fields,metadata,dependencies);
    }

}
