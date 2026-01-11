package com.banyan.compiler.lint;

import java.util.List;

public class ChallengeLinter implements Linter{


    @Override
    public List<LintFinding> lint(String json) {

        return List.of(
                new LintFinding(
                        "CHALLENGE_LINTER_ERROR: L50001",
                        "NO CHALLENGE LINTER IMPLEMENTATIONS"
                )
        );
    }
}
