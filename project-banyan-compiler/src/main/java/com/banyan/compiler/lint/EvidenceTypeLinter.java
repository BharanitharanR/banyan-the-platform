package com.banyan.compiler.lint;

import java.util.List;

public final class EvidenceTypeLinter implements Linter{

    @Override
    public List<LintFinding> lint(String json) {

        return List.of(
            new LintFinding(
                    "EVIDENCE_TYPE_LINTER_IMPLEMENTATION",
                    "EVIDENCE_TYPE_LINTER_UNIMPLEMENTED"
            )
        );
    }
}
