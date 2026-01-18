package com.banyan.compiler.backend.rule;

import com.banyan.compiler.backend.api.CompilationErrorCode;
import com.banyan.compiler.backend.api.CompilationException;
import com.banyan.compiler.enums.EvidenceValueType;
import com.fasterxml.jackson.databind.JsonNode;

public final class ValueCoercion {

    public static Object coerce(EvidenceValueType type, JsonNode value) {
        try {
            return switch (type) {
                case INTEGER -> value.asInt();
                case DECIMAL -> value.decimalValue();
                case BOOLEAN -> value.asBoolean();
                case STRING -> value.asText();
                default -> throw new IllegalArgumentException();
            };
        } catch (Exception e) {
            throw new CompilationException(
                    CompilationErrorCode.INTERNAL_COMPILER_ERROR,
                    "Value '" + value + "' incompatible with type " + type
            );
        }
    }
}
