package com.banyan.compiler.backend.ruleset;

public record RuleRefNode(
        String ruleId,
        int version
) implements RulesetExpression {}
