package com.banyan.compiler.lint;

import java.util.List;

public interface Linter {
    List<LintFinding> lint(String json);
}
