package com.banyan.compiler.core;

import com.banyan.compiler.pipeline.*;
import com.banyan.compiler.registry.CompilationPipelineRegistry;
import com.banyan.compiler.testutil.TestResourceLoader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Phase2FrontendCompilerTest {

    private final BanyanCompiler compiler;

    public Phase2FrontendCompilerTest() {
        CompilationPipelineRegistry registry = new CompilationPipelineRegistry();
        registry.register("EvidenceType", new EvidenceTypeCompilationPipeline());
        registry.register("Rule", new RuleCompilationPipeline());
        registry.register("Ruleset", new RuleSetCompilationPipeline());
        registry.register("Task", new TaskCompilationPipeline());
        registry.register("Challenge", new ChallengeCompilationPipeline());

        this.compiler = new BanyanCompiler(registry);
    }

    @Test
    void phase2_frontend_compiler_accepts_all_valid_dsls() {

        List<String> dsls =
                TestResourceLoader.loadJsonFiles("phase2/valid");

        for (String json : dsls) {
            CompilationResult result = compiler.compile(json);

            assertTrue(
                    result.isSuccess(),
                    () -> "Expected successful compilation but got errors: "
                            + result.getErrors()
                            + "\nDSL:\n"
                            + json
            );
        }
    }
}
