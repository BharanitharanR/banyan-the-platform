package com.banyan.compiler.testutil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

public final class JsonAssert {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final JsonNode root;

    public JsonAssert(String json) {
        try {
            this.root = mapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void assertFieldExists(String jsonPointer) {
        JsonNode node = root.at(jsonPointer);
        assertFalse(node.isMissingNode(),
                "Expected field to exist at: " + jsonPointer);
    }

    public void assertBooleanTrue(String jsonPointer) {
        JsonNode node = root.at(jsonPointer);
        assertTrue(node.isBoolean() && node.asBoolean(),
                "Expected true at: " + jsonPointer);
    }

    public void assertEquals(String jsonPointer, String expected) {
        JsonNode node = root.at(jsonPointer);
        Assertions.assertEquals(expected, node.asText(),
                "Mismatch at: " + jsonPointer);
    }
}
