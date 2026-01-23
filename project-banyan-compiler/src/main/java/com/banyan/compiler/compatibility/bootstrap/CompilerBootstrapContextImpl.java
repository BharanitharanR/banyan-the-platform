package com.banyan.compiler.compatibility.bootstrap;

import java.util.HashMap;
import java.util.Map;

public final class CompilerBootstrapContextImpl
        implements CompilerBootstrapContext {


    private final Map<Key, CompatibilityResolver<?, ?>> resolvers =
            new HashMap<>();

    private boolean frozen = false;


    @Override
    public <A, B> void addCompatibilityResolver(Class<A> left, Class<B> right, CompatibilityResolver<A,B> resolver) {
        assertNotFrozen();
        this.resolvers.put(new Key(left, right), resolver);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A, B> CompatibilityResolver compatibility(Class<A> left, Class<B> right) {
        return (CompatibilityResolver<A,B>) this.resolvers.get(new Key(left,right));
    }

    private void assertNotFrozen() {
        if (frozen) {
            throw new IllegalStateException("BOOTSTRAP_ALREADY_FROZEN");
        }
    }
    public  void freeze() { this.frozen = true;}
    private record Key(Class<?> left, Class<?> right) {}
}
