package com.banyan.compiler.backend.context;

import com.banyan.compiler.backend.api.CompilationErrorCode;
import com.banyan.compiler.backend.api.CompilationException;
import com.banyan.compiler.backend.api.CompiledArtifact;
import com.banyan.compiler.compatibility.bootstrap.CompatibilityResolver;
import com.banyan.compiler.compatibility.bootstrap.CompilerBootstrapContext;
import com.banyan.compiler.enums.ArtifactType;
import io.smallrye.common.constraint.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CompilationContext {
    @NotNull
    private final Map<String, CompiledArtifact> symbolTable= new HashMap<>();

    private final CompilerBootstrapContext compatibility;

    public CompilationContext(CompilerBootstrapContext compatibility) {
        this.compatibility = compatibility;
    }

    public <A, B> CompatibilityResolver<A, B> compatibility(
            Class<A> left,
            Class<B> right
    ) {
        return compatibility.compatibility(left, right);
    }

    public void register(CompiledArtifact artifact){
        symbolTable.put(key(artifact.type().toString(),artifact.id(),artifact.version()),artifact);
    }

    public CompiledArtifact resolve(ArtifactType type, String id, int version) throws CompilationException
    {
        if(!symbolTable.containsKey(key(type.toString(), id, version)))
        {
            throw new CompilationException(CompilationErrorCode.MISSING_DEPENDENCY,"KEY NOT FOUND");
        }
        return symbolTable.get(key(type.toString(), id, version)) ;
    }

    public String key(String kind,String id,int version)
    {
        return kind+":"+id+":"+version;
    }
}
