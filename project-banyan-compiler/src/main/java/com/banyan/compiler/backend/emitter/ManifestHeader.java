package com.banyan.compiler.backend.emitter;

public record ManifestHeader(String filename,String ver,String timestmp,String compVersion) {
    static String fileName;
    static String version;
    static String timestamp;
    static String compilerVersion;
}
