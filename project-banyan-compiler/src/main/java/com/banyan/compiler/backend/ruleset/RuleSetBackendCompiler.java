package com.banyan.compiler.backend.ruleset;

import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationErrorCode;
import com.banyan.compiler.backend.api.CompilationException;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.backend.context.CompilationContext;
import com.banyan.compiler.backend.spi.AbstractBackendCompiler;
import com.banyan.compiler.enums.ArtifactType;
import com.banyan.compiler.enums.LogicalOperator;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public final class RuleSetBackendCompiler
            extends AbstractBackendCompiler<CompiledRulesetArtifact> {

    private final List<ArtifactReference> dependencies = new ArrayList<>();
    @Override
    public CompiledRulesetArtifact compile(JsonNode dsl, CompilationContext context) {

        String id = readId(dsl);
        int version = readVersion(dsl);
        CompilationMetadata metadata = metadata(dsl);

        JsonNode spec = dsl.at("/spec");

        RulesetExpression root;

        // -------- Case 1: Explicit expression --------
        if (spec.has("expression")) {
            root = parseExpression(spec.get("expression"),context);
        }
        // -------- Case 2: Implicit single-rule ruleset --------
        else if (spec.has("ruleRef")) {
            root = new RuleRefNode(spec.get("ruleRef").asText(),spec.get("version").asInt());
        }
        else {
            throw new IllegalStateException(
                    "RULESET_COMPILATION_ERROR: spec must contain 'expression' or 'ruleRef'"
            );
        }

        return new CompiledRulesetArtifact(
                id,
                version,
                new CompiledRuleset(root),
                metadata,
                this.dependencies
        );
    }

    // ---------------- Expression parsing ----------------

    private RulesetExpression parseExpression(JsonNode expr,CompilationContext context) {

        // Leaf rule reference
        if (expr.has("ruleRef")) {
            String ruleId = expr.get("ruleRef").asText();
            int ruleVersion = expr.get("version").asInt();
            try {

                context.resolve(
                        ArtifactType.Rule,
                        ruleId,
                        ruleVersion
                );
                this.dependencies.add(new ArtifactReference(
                        ArtifactType.Rule,
                        ruleId,
                        ruleVersion
                ));
            }
            catch(CompilationException e)
            {
                throw new CompilationException(
                        CompilationErrorCode.MISSING_DEPENDENCY,
                        ArtifactType.Ruleset,
                        ruleId,
                        ruleVersion,
                        "Referenced Rule not found: " + ruleId + "@" + ruleVersion
                );
            }
            return new RuleRefNode(expr.get("ruleRef").asText(),expr.get("version").asInt());
        }

        // Composite node
        if (expr.has("operator") && expr.has("operands")) {

            LogicalOperator operator =
                    LogicalOperator.valueOf(expr.get("operator").asText());

            List<RulesetExpression> operands = new ArrayList<>();
            for (JsonNode operand : expr.get("operands")) {
                operands.add(parseExpression(operand,context));
            }

            return new LogicalNode(operator, operands);
        }

        throw new IllegalStateException(
                "INVALID_RULESET_EXPRESSION: " + expr.toString()
        );
    }
}