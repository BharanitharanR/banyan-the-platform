package com.banyan.compiler.backend.evidence;

import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.backend.api.CompiledArtifact;
import com.banyan.compiler.enums.ArtifactType;
import lombok.Getter;

import java.util.Map;

@Getter
public final class CompiledEvidenceTypeArtifact implements CompiledArtifact {

    private final String id;
    private final int version;
    private final Map<String, EvidenceField> fields;
    public CompiledEvidenceTypeArtifact(String id,int version,Map<String,EvidenceField> fields) {
        this.id = id;
        this.version = version;
        this.fields = fields;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public ArtifactType type() {
        return ArtifactType.EvidenceType;
    }

    @Override
    public int version() {
        return this.version;
    }


}
