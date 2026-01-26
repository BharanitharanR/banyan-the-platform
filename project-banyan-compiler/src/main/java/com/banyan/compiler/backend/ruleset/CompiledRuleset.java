package com.banyan.compiler.backend.ruleset;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = CompiledRulesetSerializer.class)
public final class CompiledRuleset {

    private final RulesetExpression root;

    public CompiledRuleset(RulesetExpression root) {
        this.root = root;
    }

    public RulesetExpression root() {
        return root;
    }
}
