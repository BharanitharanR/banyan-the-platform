package com.banyan.compiler.backend.ruleset;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "nodeType" // Discriminator field in JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LogicalNode.class, name = "LOGICAL"),
        @JsonSubTypes.Type(value = RuleRefNode.class, name = "RULE")
})
public sealed interface RulesetExpression
        permits RuleRefNode, LogicalNode {
}
