package com.banyan.compiler.backend.evidence;

import com.banyan.compiler.backend.api.CompiledArtifact;
import com.banyan.compiler.backend.context.CompilationContext;
import com.banyan.compiler.backend.spi.AbstractBackendCompiler;
import com.banyan.compiler.enums.EvidenceValueType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class EvidenceBackendCompiler extends AbstractBackendCompiler<CompiledEvidenceTypeArtifact> {
    @Override
    public CompiledEvidenceTypeArtifact compile(JsonNode validatedDsl, CompilationContext context) {
        String id = validatedDsl.get("id").asText();
        int version = validatedDsl.get("version").asInt();
        Map<String, EvidenceField> fields = new HashMap<>();
        for (JsonNode f : validatedDsl.at("/spec/fields")) {
            fields.put(
                    f.get("name").asText(),
                    new EvidenceField(
                            f.get("name").asText(),
                            EvidenceValueType.valueOf(f.get("type").asText()),
                            f.get("required").asBoolean()
                    )
            );
        }
        return new CompiledEvidenceTypeArtifact(id,version,fields);
    }
}
