package com.banyan.compiler.semantics;

import java.util.List;

public interface SemanticValidator {
    List<String> validate(String json);
}
