package com.banyan.compiler.lint;

import java.util.List;

public class TaskLinter implements Linter{


    @Override
    public List<LintFinding> lint(String json) {

        return List.of(
                new LintFinding(
                        "TASK_LINTER_ERROR: L40001",
                        "NO TASK LINTER IMPLEMENTATIONS"
                )
        );
    }
}
