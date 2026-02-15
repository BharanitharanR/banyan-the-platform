package com.banyan.platform.artifact.deserializer;
import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.backend.evidence.CompiledEvidenceType;
import com.banyan.compiler.backend.evidence.CompiledEvidenceTypeArtifact;
import com.banyan.compiler.backend.evidence.EvidenceField;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CompiledEvidenceTypeArtifactDeserializer
        implements ArtifactDeserializer<CompiledEvidenceTypeArtifact> {

    private final ObjectMapper mapper;

    public CompiledEvidenceTypeArtifactDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    /*

{
  "id" : "CONSOLIDATED_EVIDENCES",
  "version" : 1,
  "artifactType" : "EvidenceType",
  "metadata" : {
    "compilerVersion" : "banyan-compiler-2.0",
    "compiledAtEpochMillis" : 1770570292662,
    "contentHash" : "1d756b323c39b45a4dde2a7440f985ceed61bce2c98921b4e6b884b1d8751db2"
  },
  "dependencies" : [ {
    "type" : "EvidenceType",
    "id" : "CONSOLIDATED_EVIDENCES",
    "version" : 1
  } ],
  "payload" : {
    "id" : "CONSOLIDATED_EVIDENCES",
    "version" : 1,
    "fields" : [ {
      "name" : "failedAttempts",
      "type" : "INTEGER",
      "required" : true
    }, {
      "name" : "score",
      "type" : "INTEGER",
      "required" : true
    }, {
      "name" : "userType",
      "type" : "INTEGER",
      "required" : true
    }, {
      "name" : "country",
      "type" : "BOOLEAN",
      "required" : true
    }, {
      "name" : "businessHours",
      "type" : "BOOLEAN",
      "required" : true
    } ]
  }
}
     */
    @Override
    public CompiledEvidenceTypeArtifact deserialize(JsonNode node) {

        String id = node.get("id").asText();
        int version = node.get("version").asInt();
        // Runtime does NOT need full compilation metadata
        CompilationMetadata metadata = null; // or EMPTY
        JsonNode simplfiedPayload = node.get("payload");
        String evidenceTypeId = simplfiedPayload.get("id").asText();
        Integer evidenceTypeVersion = simplfiedPayload.get("version").asInt();
        List<EvidenceField> evidenceFields = mapper.convertValue(
                simplfiedPayload.get("fields"),
                new TypeReference<List<EvidenceField>>() {
                }
        );
        Map<String, EvidenceField> evidenceFieldMap =
                evidenceFields.stream()
                        .collect(Collectors.toUnmodifiableMap(
                                EvidenceField::name,
                                field -> field
                        ));



        List<ArtifactReference> deps =
                mapper.convertValue(
                        node.get("dependencies"),
                        new TypeReference<List<ArtifactReference>>() {}
                );

        return new CompiledEvidenceTypeArtifact(
                id,
                version,
                new CompiledEvidenceType(evidenceTypeId,evidenceTypeVersion,evidenceFieldMap),
                metadata,
                deps
        );
    }
}
