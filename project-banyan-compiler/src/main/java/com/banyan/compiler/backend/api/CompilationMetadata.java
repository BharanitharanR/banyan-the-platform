package com.banyan.compiler.backend.api;


import lombok.Data;

@Data
public final class CompilationMetadata {
    private final String compilerVersion;
    private final long compiledAtEpochMillis;
    private final String contentHash;
    public CompilationMetadata(
            String compilerVersion,
            long compiledAtEpochMillis,
            String contentHash
    ){
        this.compiledAtEpochMillis = compiledAtEpochMillis;
        this.compilerVersion = compilerVersion;
        this.contentHash = contentHash;
    }
}
