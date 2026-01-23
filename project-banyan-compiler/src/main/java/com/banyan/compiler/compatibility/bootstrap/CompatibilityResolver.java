package com.banyan.compiler.compatibility.bootstrap;

import java.util.Set;

public interface CompatibilityResolver<A,B> {
    boolean isCompatible(A left,B right);

    Set<B> supportedRHS(A left);

    Set<A> supportedLHS(B right);

}
