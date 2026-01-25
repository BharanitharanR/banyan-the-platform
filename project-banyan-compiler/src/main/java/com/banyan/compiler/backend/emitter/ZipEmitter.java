package com.banyan.compiler.backend.emitter;

import com.banyan.compiler.backend.api.CompiledArtifact;
import com.banyan.compiler.backend.outcome.CompilationOutcome;
import com.banyan.compiler.backend.outcome.CompilationRoot;

public class ZipEmitter implements ArtifactEmitter{
    @Override
    public boolean supports(CompilationRoot root) {
        return false;
    }

    @Override
    public void emit(CompilationOutcome outcome) {
        if(outcome.isSuccess())
        {
            outcome.getReachableArtifacts().stream().forEach(compiledArtifact -> {
               compiledArtifact.payload();
            });
        }
    }

    public void saveFile()
    {

    }
}
