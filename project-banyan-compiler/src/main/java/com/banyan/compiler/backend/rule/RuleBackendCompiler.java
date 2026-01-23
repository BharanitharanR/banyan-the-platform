package com.banyan.compiler.backend.rule;
import com.banyan.compiler.backend.api.CompilationErrorCode;
import com.banyan.compiler.backend.api.CompilationException;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.backend.context.CompilationContext;
import com.banyan.compiler.backend.evidence.CompiledEvidenceTypeArtifact;
import com.banyan.compiler.backend.evidence.EvidenceField;
import com.banyan.compiler.backend.spi.AbstractBackendCompiler;
import com.banyan.compiler.compatibility.bootstrap.CompatibilityResolver;
import com.banyan.compiler.enums.ArtifactType;
import com.banyan.compiler.enums.EvidenceValueType;
import com.banyan.compiler.enums.RuleType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class RuleBackendCompiler extends AbstractBackendCompiler<CompiledRuleArtifact> {
    @Override
    public CompiledRuleArtifact compile(JsonNode dsl, CompilationContext context) {
        String id = readId(dsl);
        int version = readVersion(dsl);
        CompilationMetadata metadata = metadata(dsl);

        JsonNode spec = dsl.at("/spec");

        // 1. Resolve EvidenceType
        String evidenceId = spec.at("/evidenceTypeRef/id").asText();
        int evidenceVersion = spec.at("/evidenceTypeRef/version").asInt();

        CompiledEvidenceTypeArtifact evidenceArtifact =
                (CompiledEvidenceTypeArtifact) context.resolve(
                        ArtifactType.EvidenceType,
                        evidenceId,
                        evidenceVersion
                );
        CompatibilityResolver<RuleType, EvidenceValueType> resolver =
                context.compatibility(RuleType.class, EvidenceValueType.class);



        Map<String, EvidenceField> fields = evidenceArtifact.payload().fields();

        // 2. Validate input exists
        String input = spec.at("/input").asText();
        EvidenceField field = fields.get(input);
        if (field == null) {
            throw new CompilationException(
                    CompilationErrorCode.INTERNAL_COMPILER_ERROR,
                    "Field '" + input + "' not found in EvidenceType "
                            + evidenceId + "@" + evidenceVersion
            );
        }


        // 3. Validate type matches EXACTLY
        RuleType declaredType =
                RuleType.valueOf(spec.at("/type").asText());


        if (!resolver.isCompatible(declaredType,field.type())) {
            throw new CompilationException(
                    CompilationErrorCode.CONTEXT_CORRUPTED,
                    declaredType + " not compatible with " + field.type()
            );
        } ;


        return new CompiledRuleArtifact(id,version,    new CompiledRule(
                input,
                spec.get("operator").asText(),
                extractValue(spec.get("value")),
                spec.get("type").asText()
        ),metadata);
    }

    private Object extractValue(JsonNode node) {
        if (node.isInt()) return node.intValue();
        if (node.isLong()) return node.longValue();
        if (node.isDouble()) return node.doubleValue();
        if (node.isBoolean()) return node.booleanValue();
        if (node.isTextual()) return node.textValue();
        throw new IllegalStateException("UNSUPPORTED_RULE_VALUE");
    }

}
