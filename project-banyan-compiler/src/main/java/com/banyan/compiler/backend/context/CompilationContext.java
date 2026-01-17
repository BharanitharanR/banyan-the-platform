package com.banyan.compiler.backend.context;

import com.banyan.compiler.backend.api.CompiledArtifact;
import com.banyan.compiler.enums.ArtifactType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CompilationContext {
    private final Map<String, CompiledArtifact> symbolTable= new HashMap<>();

    public void register(CompiledArtifact artifact){
        symbolTable.put(key(artifact.type().toString(),artifact.id(),artifact.version()),artifact);
    }

    public CompiledArtifact resolve(ArtifactType type, String id, int version)
    {
        return symbolTable.get(key(type.toString(), id, version));
    }

    public String key(String kind,String id,int version)
    {
        return kind+":"+id+":"+version;
    }
}
