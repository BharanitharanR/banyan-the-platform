package com.banyan.platform.artifact.deserializer;
import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.backend.challenge.CompiledChallenge;
import com.banyan.compiler.backend.challenge.CompiledChallengeArtifact;
import com.banyan.compiler.backend.challenge.CompiledTaskRef;
import com.banyan.compiler.backend.ruleset.CompiledRulesetArtifact;
import com.banyan.compiler.backend.task.CompiledTask;
import com.banyan.compiler.backend.task.CompiledTaskArtifact;
import com.banyan.compiler.backend.task.TaskActionRecord;
import com.banyan.compiler.enums.TaskResulTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public final class CompiledTaskArtifactDeserializer
        implements ArtifactDeserializer<CompiledTaskArtifact> {

    private final ObjectMapper mapper;

    public CompiledTaskArtifactDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }
/*
    "ruleSetId" : "login_ruleset",
    "rulesetVersion" : 1,
    "taskType" : "BOOLEAN",
    "taskActions" : [ {
      "taskActionOn" : "FAILURE",
      "action" : "LOCK_ACCOUNT"
    }, {
      "taskActionOn" : "ALWAYS",
      "action" : "AUDIT_EVENT"
    } ],
    "description" : "Login security task"

public record CompiledTask(String ruleSetId, Integer rulesetVersion, TaskResulTypeEnum taskType, List<TaskActionRecord> taskActions, String description) {
}

 */
    @Override
    public CompiledTaskArtifact deserialize(JsonNode node) {

        String id = node.get("id").asText();
        int version = node.get("version").asInt();
        // Runtime does NOT need full compilation metadata
        CompilationMetadata metadata = null; // or EMPTY
        JsonNode simplfiedPayload = node.get("payload");
        String ruleSetId = simplfiedPayload.get("ruleSetId").asText();
        Integer ruleSetVersion = simplfiedPayload.get("rulesetVersion").asInt();
        TaskResulTypeEnum taskType  =  TaskResulTypeEnum.valueOf(simplfiedPayload.get("taskType").asText().trim());
        List<TaskActionRecord> taskActions = mapper.convertValue(
                simplfiedPayload.get("taskActions"),
                new TypeReference<List<TaskActionRecord>>() {
                }
        );
        String description = simplfiedPayload.get("description").asText();

        List<ArtifactReference> deps =
                mapper.convertValue(
                        node.get("dependencies"),
                        new TypeReference<List<ArtifactReference>>() {}
                );

        return new CompiledTaskArtifact(
                id,
                version,
                new CompiledTask(ruleSetId,ruleSetVersion,taskType,taskActions,description),
                metadata,
                deps
        );
    }
}
