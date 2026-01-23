package com.banyan.compiler.compatibility.bootstrap;

public interface CompilerBootstrapContext {

    <A, B> void addCompatibilityResolver(
            Class<A> left,
            Class<B> right,
            CompatibilityResolver<A, B> resolver
    );



    <A, B> CompatibilityResolver<A, B> compatibility(
            Class<A> left,
            Class<B> right
    );
}
