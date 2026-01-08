package com.banyan.compiler.lint;

import java.util.List;

public class RuleSetLinter implements Linter{


    @Override
    public List<LintFinding> lint(String json) {

        return List.of(
                new LintFinding(
                        "RULESET_LINTER_ERROR: L30001",
                        "NO RULESET LINTER IMPLEMENTATIONS"
                )
        );
    }
}
