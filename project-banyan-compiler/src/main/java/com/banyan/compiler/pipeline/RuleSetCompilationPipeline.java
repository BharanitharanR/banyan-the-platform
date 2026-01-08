package com.banyan.compiler.pipeline;

import com.banyan.compiler.core.CompilationPipeline;
import com.banyan.compiler.lint.Linter;
import com.banyan.compiler.lint.RuleSetLinter;
import com.banyan.compiler.schema.RuleSetSchemaValidator;
import com.banyan.compiler.schema.SchemaValidator;
import com.banyan.compiler.semantics.RuleSetSemanticValidator;
import com.banyan.compiler.semantics.SemanticValidator;

public class RuleSetCompilationPipeline implements CompilationPipeline {

    SchemaValidator schemaValidator = new RuleSetSchemaValidator();

    SemanticValidator semanticValidator = new RuleSetSemanticValidator();

    Linter linter = new RuleSetLinter();
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
