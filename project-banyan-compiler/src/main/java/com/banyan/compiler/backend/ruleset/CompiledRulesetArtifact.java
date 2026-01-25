package com.banyan.compiler.backend.ruleset;

import com.banyan.compiler.backend.api.AbstractCompiledArtifact;
import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.enums.ArtifactType;

import java.util.List;

public class CompiledRulesetArtifact extends AbstractCompiledArtifact<CompiledRuleset> {
    public CompiledRulesetArtifact(String id, int version, CompiledRuleset field, CompilationMetadata metadata, List<ArtifactReference> dependencies) {
        super(id,version, ArtifactType.Ruleset,field,metadata,dependencies);
    }

}