package com.banyan.platform.deserializer;

//public class CompiledRuleArtifactDeserializer {
//}

import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.backend.rule.CompiledRule;
import com.banyan.compiler.backend.rule.CompiledRuleArtifact;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
/*
{
  "id" : "within_business_hours",
  "version" : 2,
  "artifactType" : "Rule",
  "metadata" : {
    "compilerVersion" : "banyan-compiler-2.0",
    "compiledAtEpochMillis" : 1770570292670,
    "contentHash" : "7a495cca94d08f2286536f4558a1ac26279bd4acba37d222a20913c3975abcb9"
  },
  "dependencies" : [ {
    "type" : "EvidenceType",
    "id" : "CONSOLIDATED_EVIDENCES",
    "version" : 1
  } ],
  "payload" : {
    "input" : "businessHours",
    "operator" : "==",
    "value" : true,
    "ruleType" : "EQUALITY"
  }
}
 */
public final class CompiledRuleArtifactDeserializer
        implements ArtifactDeserializer<CompiledRuleArtifact> {

    private final ObjectMapper mapper;

    public CompiledRuleArtifactDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CompiledRuleArtifact deserialize(JsonNode node) {

        String id = node.get("id").asText();
        int version = node.get("version").asInt();
        // Runtime does NOT need full compilation metadata
        CompilationMetadata metadata = null; // or EMPTY
        JsonNode simplfiedPayload = node.get("payload");
        String input = simplfiedPayload.get("input").asText().trim();
        String operator = simplfiedPayload.get("operator").asText().trim();
        String value = simplfiedPayload.get("value").asText().trim();
        String ruleType = simplfiedPayload.get("ruleType").asText().trim();
        List<ArtifactReference> deps =
                mapper.convertValue(
                        node.get("dependencies"),
                        new TypeReference<List<ArtifactReference>>() {}
                );

        return new CompiledRuleArtifact(
                id,
                version,
                new CompiledRule(input,operator,value,ruleType),
                metadata,
                deps
        );
    }
}
