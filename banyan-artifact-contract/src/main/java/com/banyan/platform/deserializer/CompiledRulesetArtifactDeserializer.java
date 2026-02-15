package com.banyan.platform.deserializer;

import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.backend.ruleset.CompiledRuleset;
import com.banyan.compiler.backend.ruleset.CompiledRulesetArtifact;
import com.banyan.compiler.backend.ruleset.RulesetExpression;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
/*
{
  "id" : "login_ruleset",
  "version" : 1,
  "artifactType" : "Ruleset",
  "metadata" : {
    "compilerVersion" : "banyan-compiler-2.0",
    "compiledAtEpochMillis" : 1770570292674,
    "contentHash" : "ec3e3d55b4b562f818271ca35d42cf7ac345d88926e2fddf5dc0c2fd099feab5"
  },
  "dependencies" : [ {
    "type" : "Rule",
    "id" : "max_failed_attempts",
    "version" : 1
  }, {
    "type" : "Rule",
    "id" : "max_incorrect_passwords_history_greater_than_three",
    "version" : 1
  }, {
    "type" : "Rule",
    "id" : "within_business_hours",
    "version" : 2
  }, {
    "type" : "Rule",
    "id" : "ip_not_blacklisted",
    "version" : 1
  } ],
  "payload" : {
    "expression" : {
      "nodeType" : "LOGICAL",
      "operator" : "OR",
      "operands" : [ {
        "nodeType" : "LOGICAL",
        "operator" : "AND",
        "operands" : [ {
          "nodeType" : "LOGICAL",
          "operator" : "OR",
          "operands" : [ {
            "nodeType" : "RULE",
            "ruleId" : "max_failed_attempts",
            "version" : 1
          }, {
            "nodeType" : "RULE",
            "ruleId" : "max_incorrect_passwords_history_greater_than_three",
            "version" : 1
          } ]
        }, {
          "nodeType" : "RULE",
          "ruleId" : "within_business_hours",
          "version" : 2
        } ]
      }, {
        "nodeType" : "RULE",
        "ruleId" : "ip_not_blacklisted",
        "version" : 1
      } ]
    }
  }
}
 */
public final class CompiledRulesetArtifactDeserializer
        implements ArtifactDeserializer<CompiledRulesetArtifact> {

    private final ObjectMapper mapper;

    public CompiledRulesetArtifactDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CompiledRulesetArtifact deserialize(JsonNode node) {

        String id = node.get("id").asText();
        int version = node.get("version").asInt();
        // Runtime does NOT need full compilation metadata
        CompilationMetadata metadata = null; // or EMPTY
        JsonNode simplfiedPayload = node.get("payload");
        RulesetExpression challenge = mapper.convertValue(
                simplfiedPayload.get("expression"),
                new TypeReference<RulesetExpression>() {
                }
        );
        List<ArtifactReference> deps =
                mapper.convertValue(
                        node.get("dependencies"),
                        new TypeReference<List<ArtifactReference>>() {}
                );

        return new CompiledRulesetArtifact(
                id,
                version,
                new CompiledRuleset(challenge),
                metadata,
                deps
        );
    }
}
