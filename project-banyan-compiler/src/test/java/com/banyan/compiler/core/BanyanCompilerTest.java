package com.banyan.compiler.core;

import com.banyan.compiler.pipeline.*;
import com.banyan.compiler.registry.CompilationPipelineRegistry;
import com.banyan.compiler.testutil.TestResourceLoader;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BanyanCompilerTest {
    public CompilationPipelineRegistry registry = new CompilationPipelineRegistry();
    public BanyanCompiler compiler;
    public BanyanCompilerTest(){
        this.registry.register("Rule",new RuleCompilationPipeline());
        this.registry.register("Ruleset",new RuleSetCompilationPipeline());
        this.registry.register("EvidenceType",new EvidenceTypeCompilationPipeline());
        this.registry.register("Task",new TaskCompilationPipeline());
        this.registry.register("Challenge",new ChallengeCompilationPipeline());
        this.compiler = new BanyanCompiler(this.registry);
    }

    private static final String EVIDENCE_VALID_RESOURCE = "evidence-type/schema/valid";
    private static final String EVIDENCE_INVALID_RESOURCE = "evidence-type/schema/invalid";
    private static final String RULE_VALID_RESOURCE = "rule/schema/valid";
    private static final String RULE_INVALID_RESOURCE = "rule/schema/invalid";
    private static final String RULESET_VALID_RESOURCE = "ruleset/schema/valid";
    private static final String RULESET_INVALID_RESOURCE = "ruleset/schema/invalid";
    private static final String TASK_VALID_RESOURCE = "task/schema/valid";
    private static final String TASK_INVALID_RESOURCE = "task/schema/invalid";
    private static final String CHALLENGE_VALID_RESOURCE = "challenge/schema/valid";
    private static final String CHALLENGE_INVALID_RESOURCE = "challenge/schema/invalid";


    @Test
    void allValidRuleSetShouldPass() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(RULESET_VALID_RESOURCE);

        for (String json : jsons) {
            CompilationResult result = this.compiler.compile(json);
            System.out.println(json);
            result.getErrors().forEach(System.out::println);
           // assertTrue(result.isSuccess());
        }
    }

    @Test
    void allInvalidRuleSetShouldFail() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(RULESET_INVALID_RESOURCE);

        for (String json : jsons) {

            CompilationResult result = this.compiler.compile(json);
           // assertTrue(!result.isSuccess());
        }
    }

    @Test
    void allValidChallengeShouldPass() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(CHALLENGE_VALID_RESOURCE);

        for (String json : jsons) {
            CompilationResult result = this.compiler.compile(json);
            System.out.println(json);
            result.getErrors().forEach(System.out::println);
           // assertTrue(result.isSuccess());
        }
    }

    @Test
    void allInvalidChallengeShouldFail() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(CHALLENGE_INVALID_RESOURCE);

        for (String json : jsons) {

            CompilationResult result = this.compiler.compile(json);
            assertTrue(!result.isSuccess());
        }
    }


    @Test
    void allValidTaskShouldPass() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(TASK_VALID_RESOURCE);

        for (String json : jsons) {
            CompilationResult result = this.compiler.compile(json);
            System.out.println(json);
            result.getErrors().forEach(System.out::println);
            assertTrue(result.isSuccess());
        }
    }

    @Test
    void allInvalidTaskShouldFail() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(TASK_INVALID_RESOURCE);

        for (String json : jsons) {

            CompilationResult result = this.compiler.compile(json);
            assertTrue(!result.isSuccess());
        }
    }

    @Test
    void allValidRulesShouldPass() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(RULE_VALID_RESOURCE);
        for (String json : jsons) {
            CompilationResult result = this.compiler.compile(json);
            System.out.println(json);
            result.getErrors().forEach(System.out::println);
            assertTrue(result.isSuccess());
        }
    }

    @Test
    void allInvalidRulesShouldFail() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(RULE_INVALID_RESOURCE);

        for (String json : jsons) {

            CompilationResult result = this.compiler.compile(json);
            assertTrue(!result.isSuccess());
        }
    }

    @Test
    void allValidTypesShouldPass() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(EVIDENCE_VALID_RESOURCE);

        for (String json : jsons) {

            CompilationResult result = this.compiler.compile(json);
            assertTrue(result.isSuccess());
        }
    }

    @Test
    void allInvalidEvidenceTypesShouldFail() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(EVIDENCE_INVALID_RESOURCE);

        for (String json : jsons) {

            CompilationResult result = this.compiler.compile(json);
            assertTrue(!result.isSuccess());
        }
    }


}
