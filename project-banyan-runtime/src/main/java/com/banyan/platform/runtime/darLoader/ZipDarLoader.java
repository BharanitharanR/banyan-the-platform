package com.banyan.platform.runtime.darLoader;


import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.banyan.compiler.backend.challenge.CompiledChallenge;
import com.banyan.compiler.backend.challenge.CompiledChallengeArtifact;
import com.banyan.compiler.backend.evidence.CompiledEvidenceType;
import com.banyan.compiler.backend.evidence.CompiledEvidenceTypeArtifact;
import com.banyan.compiler.backend.rule.CompiledRule;
import com.banyan.compiler.backend.rule.CompiledRuleArtifact;
import com.banyan.compiler.backend.ruleset.CompiledRuleset;
import com.banyan.compiler.backend.ruleset.CompiledRulesetArtifact;
import com.banyan.compiler.backend.task.CompiledTask;

import com.banyan.compiler.backend.task.CompiledTaskArtifact;
import com.banyan.platform.deserializer.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ZipDarLoader {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ZipDarLoader.class);
    private static  final ObjectMapper mapper = new ObjectMapper();

    public record ChallengeKey(int version, String name) {
    }

    public record TaskKey(int version, String name) {
    }

    public record RulesetKey(int version, String name) {
    }

    public record RuleKey(int version, String name) {
    }

    public record EvidenceTypeKey(int version, String name) {
    }

    public static Map<ChallengeKey, CompiledChallenge> challenges = new HashMap<>();
    public static Map<TaskKey, CompiledTask> tasks = new HashMap<>();
    public static Map<RulesetKey, CompiledRuleset> rulesets = new HashMap<>();
    public static Map<RuleKey, CompiledRule> rules = new HashMap<>();
    public static Map<EvidenceTypeKey, CompiledEvidenceType> evidenceTypes = new HashMap<>();

    public static void load(String darPath) throws Exception {

        try (ZipFile zip = new ZipFile(darPath)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) continue;
                String name = entry.getName();
                InputStream in = zip.getInputStream(entry);
                CompiledChallengeArtifactDeserializer CHALLENGE = new CompiledChallengeArtifactDeserializer(mapper);
                CompiledTaskArtifactDeserializer TASK = new CompiledTaskArtifactDeserializer(mapper);
                CompiledRulesetArtifactDeserializer RULESET = new CompiledRulesetArtifactDeserializer(mapper);
                CompiledRuleArtifactDeserializer RULE = new CompiledRuleArtifactDeserializer(mapper);
                CompiledEvidenceTypeArtifactDeserializer EVIDENCETYPE = new CompiledEvidenceTypeArtifactDeserializer(mapper);
                if (name.startsWith("Challenge/")) {
                    LOGGER.info(name);
                    JsonNode names = mapper.readTree(in);
                    CompiledChallengeArtifact artifact = CHALLENGE.deserialize(names);
                    artifact.dependencies().forEach(task->{System.out.println(task.id()+task.type()+task.version());});
                    challenges.put(new ChallengeKey(artifact.version(),artifact.id()),artifact.payload());
                } else if (name.startsWith("Task/")) {
                    LOGGER.info(name);
                    JsonNode names = mapper.readTree(in);
                    CompiledTaskArtifact artifact = TASK.deserialize(names);
                    artifact.dependencies().forEach(task->{System.out.println(task.id()+task.type()+task.version());});
                    tasks.put(new TaskKey(artifact.version(), artifact.id()),artifact.payload());

                } else if (name.startsWith("Ruleset/")) {
                    LOGGER.info(name);
                    JsonNode names = mapper.readTree(in);
                    CompiledRulesetArtifact artifact = RULESET.deserialize(names);
                    artifact.dependencies().forEach(task->{System.out.println(task.id()+task.type()+task.version());});
                    rulesets.put(new RulesetKey(artifact.version(), artifact.id()),artifact.payload());
                } else if (name.startsWith("Rule/")) {
                    LOGGER.info(name);
                    JsonNode names = mapper.readTree(in);
                    CompiledRuleArtifact artifact = RULE.deserialize(names);
                    artifact.dependencies().forEach(task->{System.out.println(task.id()+task.type()+task.version());});
                    rules.put(new RuleKey(artifact.version(), artifact.id()),artifact.payload());
                } else if (name.startsWith("EvidenceType/")) {
                    LOGGER.info(name);
                    JsonNode names = mapper.readTree(in);
                    CompiledEvidenceTypeArtifact artifact = EVIDENCETYPE.deserialize(names);
                    artifact.dependencies().forEach(task->{System.out.println(task.id()+task.type()+task.version());});
                    evidenceTypes.put(new EvidenceTypeKey(artifact.version(), artifact.id()),artifact.payload());
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {
        ZipDarLoader.load("/Users/bharani/Documents/task-challenge-backend/project-banyan-runtime/src/main/resources/compilation_package.dar");
        if(ZipDarLoader.challenges.isEmpty())
            LOGGER.info("No data");
    }
}
