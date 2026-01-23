package com.banyan.compiler.compatibility.bootstrap;

import com.banyan.compiler.compatibility.resolvers.RuleEvidenceCompatibilityResolver;
import com.banyan.compiler.enums.EvidenceValueType;
import com.banyan.compiler.enums.RuleType;

public final class CompilerCompatibilityBootstrap {

    public static CompilerBootstrapContext bootstrap() {

        CompilerBootstrapContextImpl ctx =
                new CompilerBootstrapContextImpl();

        ctx.addCompatibilityResolver(
                RuleType.class,
                EvidenceValueType.class,
                RuleEvidenceCompatibilityResolver.fromJson(
                        "/compatibility/rule-evidence.json"
                )
        );

        ctx.freeze();
        return ctx;
    }
}
