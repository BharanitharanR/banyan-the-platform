package com.banyan.compiler.backend.rule;

import com.banyan.compiler.enums.EvidenceValueType;

import java.util.Set;

public final class OperatorCompatibility {

    public static boolean supports(EvidenceValueType type, String operator) {
        return switch (type) {
            case INTEGER, DECIMAL -> Set.of(">", "<", ">=", "<=", "==", "!=").contains(operator);
            case BOOLEAN -> Set.of("==", "!=").contains(operator);
            case STRING -> Set.of("==", "!=").contains(operator);
            default -> false;
        };
    }
}
