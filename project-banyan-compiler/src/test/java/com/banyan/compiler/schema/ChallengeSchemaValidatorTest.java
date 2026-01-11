package com.banyan.compiler.schema;

import com.banyan.compiler.testutil.TestResourceLoader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChallengeSchemaValidatorTest {
              public SchemaValidator validator =  new ChallengeSchemaValidator();

    private static final String VALID_RESOURCE = "challenge/schema/valid";
    private static final String INVALID_RESOURCE = "challenge/schema/invalid";


    @Test
    void allValidChallengeShouldPass() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(VALID_RESOURCE);

        for (String json : jsons) {
            List<String> errors = validator.validate(json);
            System.out.println(json);
            assertTrue(errors.isEmpty(), "Expected no errors but got: " + errors);
        }
    }

    @Test
    void allInvalidChallengeShouldFail() {
        List<String> jsons =
                TestResourceLoader.loadJsonFiles(INVALID_RESOURCE);

        for (String json : jsons) {
            System.out.println(json);
            List<String> errors = validator.validate(json);
            errors.forEach(System.out::println);
            assertFalse(errors.isEmpty(), "Expected errors but got none");
        }
    }
}
