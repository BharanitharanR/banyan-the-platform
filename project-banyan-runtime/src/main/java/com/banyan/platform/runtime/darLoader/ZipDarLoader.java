package com.banyan.platform.runtime.darLoader;


import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.banyan.compiler.backend.api.CompiledArtifact;
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

import com.banyan.platform.runtime.CompiledChallengeArtifactDeserializer;
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

    public static void load(String darPath) throws Exception {

        Map<ChallengeKey, CompiledChallenge> challenges = new HashMap<>();
        Map<TaskKey, CompiledTask> tasks = new HashMap<>();
        Map<RulesetKey, CompiledRuleset> rulesets = new HashMap<>();
        Map<RuleKey, CompiledRule> rules = new HashMap<>();
        Map<EvidenceTypeKey, CompiledEvidenceType> evidenceTypes = new HashMap<>();

        try (ZipFile zip = new ZipFile(darPath)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) continue;

                String name = entry.getName();
                InputStream in = zip.getInputStream(entry);
                CompiledChallengeArtifactDeserializer CHALLENGE =
                        new CompiledChallengeArtifactDeserializer(mapper);
                if (name.startsWith("Challenge/")) {
                    LOGGER.info(name);
                    JsonNode names = mapper.readTree(in);
                    //CompiledArtifact<CompiledChallenge> c = mapper.readValue(in, CompiledChallengeArtifact.class);
                    CompiledChallengeArtifact artifact = CHALLENGE.deserialize(names);

                    artifact.dependencies().forEach(task->{System.out.println(task.id()+task.type()+task.version());});
                    // challenges.put(new ChallengeKey(c.compiledTaskRefsList().), c);
                } else if (name.startsWith("Task/")) {
                   // CompiledArtifact<CompiledTask> t = mapper.readValue(in, CompiledTaskArtifact.class);
                    //CompiledChallengeArtifact artifact =
                    //        ArtifactObjectMapper.mapper()
                     //               .readValue(in, CompiledChallengeArtifact.class);
                    //LOGGER.info(String.valueOf(artifact.version()));
                    // tasks.put(new TaskKey(t.version(), t.name()), t);
                } else if (name.startsWith("Ruleset/")) {
                  //  CompiledArtifact<CompiledRuleset> r = mapper.readValue(in, CompiledRulesetArtifact.class);
                    // rulesets.put(new RulesetKey(r.version(), r.name()), r);
                } else if (name.startsWith("Rule/")) {
                    //CompiledArtifact<CompiledRule> r = mapper.readValue(in, CompiledRuleArtifact.class);

                    // rules.put(new RuleKey(r.version(), r.name()), r);
                } else if (name.startsWith("EvidenceType/")) {
                   // CompiledArtifact<CompiledEvidenceType> e = mapper.readValue(in, CompiledEvidenceTypeArtifact.class);
                    //LOGGER.info(e.id());
                    //evidenceTypes.put(new EvidenceTypeKey(e.version(), e.name()), e);
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {

        ZipDarLoader.load("/Users/bharani/Documents/task-challenge-backend/project-banyan-runtime/src/main/resources/compilation_package.dar");
    }
}
