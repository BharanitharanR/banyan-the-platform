package com.banyan.compiler.pipeline;

import com.banyan.compiler.core.CompilationPipeline;
import com.banyan.compiler.lint.ChallengeLinter;
import com.banyan.compiler.lint.Linter;
import com.banyan.compiler.schema.ChallengeSchemaValidator;
import com.banyan.compiler.schema.SchemaValidator;
import com.banyan.compiler.semantics.ChallengeSemanticValidator;
import com.banyan.compiler.semantics.SemanticValidator;

public class ChallengeCompilationPipeline implements CompilationPipeline {
    public SchemaValidator schemaValidator = new ChallengeSchemaValidator();
    public SemanticValidator semanticValidator = new ChallengeSemanticValidator();
    public Linter linter = new ChallengeLinter();
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
