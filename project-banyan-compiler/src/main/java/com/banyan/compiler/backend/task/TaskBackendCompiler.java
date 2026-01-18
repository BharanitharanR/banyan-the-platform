package com.banyan.compiler.backend.task;

import com.banyan.compiler.backend.api.CompilationErrorCode;
import com.banyan.compiler.backend.api.CompilationException;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.backend.context.CompilationContext;
import com.banyan.compiler.backend.ruleset.CompiledRulesetArtifact;
import com.banyan.compiler.backend.spi.AbstractBackendCompiler;
import com.banyan.compiler.enums.ArtifactType;
import com.banyan.compiler.enums.TaskActionEnum;
import com.banyan.compiler.enums.TaskResulTypeEnum;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedList;
import java.util.List;

/*
{
  "kind": "Task",
  "id": "task_with_actions",
  "version": 1,
  "spec": {
    "rulesetRef": "login_ruleset",
    "resultType": "BOOLEAN",
    "actions": [
      { "on": "FAILURE", "emit": "LOCK_ACCOUNT" },
      { "on": "ALWAYS", "emit": "AUDIT_EVENT" }
    ],
    "description": "Login security task"
  }
}

 */
public final class TaskBackendCompiler extends AbstractBackendCompiler<CompiledTaskArtifact> {
    @Override
    public CompiledTaskArtifact compile(JsonNode validatedDsl, CompilationContext context) {

        String id = readId(validatedDsl);
        int version = readVersion(validatedDsl);
        CompilationMetadata metadata = metadata(validatedDsl);
        JsonNode spec = validatedDsl.get("spec");
        if(spec.isMissingNode() || spec.isEmpty()) {
            throw new CompilationException(
                    CompilationErrorCode.INTERNAL_COMPILER_ERROR,
                    ArtifactType.Task,
                    id,
                    version,
                    "Spec missing"
            );
        }
            String rulesetRef = spec.at("/rulesetRef/id").asText();
            TaskResulTypeEnum type = TaskResulTypeEnum.valueOf(spec.at("/resultType").asText());
            List<TaskActionRecord> actionsList = new LinkedList<>();
            JsonNode actions = spec.at("/actions");
            String description = spec.at("/description").asText();
            int rulesetVersion = spec.at("/rulesetRef/version").asInt();
            // HARD dependency check
                try {
                    context.resolve(
                            ArtifactType.Ruleset,
                            rulesetRef,
                            rulesetVersion
                    );
                } catch (CompilationException e) {
                    throw new CompilationException(
                            CompilationErrorCode.MISSING_DEPENDENCY,
                            ArtifactType.Task,
                            id,
                            version,
                            "Referenced Ruleset not found: " + rulesetRef + "@" + rulesetVersion
                    );
                }
            for(JsonNode action: actions)
            {

                actionsList.add(new TaskActionRecord(TaskActionEnum.valueOf(action.at("/on").asText()),action.at("/emit").asText()));
            }

        return  new CompiledTaskArtifact(
                id,version,new CompiledTask(rulesetRef,rulesetVersion,type,actionsList,description),metadata);

    }
}
