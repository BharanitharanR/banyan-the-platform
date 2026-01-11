package com.banyan.compiler.pipeline;

import com.banyan.compiler.core.CompilationPipeline;
import com.banyan.compiler.lint.ChallengeLinter;
import com.banyan.compiler.lint.Linter;
import com.banyan.compiler.lint.TaskLinter;
import com.banyan.compiler.schema.SchemaValidator;
import com.banyan.compiler.schema.TaskSchemaValidator;
import com.banyan.compiler.semantics.ChallengeSemanticValidator;
import com.banyan.compiler.semantics.SemanticValidator;
import com.banyan.compiler.semantics.TaskSemanticValidator;

public class TaskCompilationPipeline implements CompilationPipeline {
    private final SchemaValidator schemaValidator = new TaskSchemaValidator();
    public SemanticValidator semanticValidator = new TaskSemanticValidator();
    public Linter linter = new TaskLinter();
    @Override
    public SchemaValidator schemaValidator() {
        return this.schemaValidator;
    }

    @Override
    public SemanticValidator semanticValidator() {
        return this.semanticValidator;
    }

    @Override
    public Linter lint() {
        return this.linter;
    }
}
