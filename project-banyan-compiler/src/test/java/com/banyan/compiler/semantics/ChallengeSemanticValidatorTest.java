package com.banyan.compiler.semantics;

import com.banyan.compiler.schema.ChallengeSchemaValidator;
import com.banyan.compiler.schema.SchemaValidator;
import com.banyan.compiler.testutil.TestResourceLoader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChallengeSemanticValidatorTest {
              public SemanticValidator validator =  new ChallengeSemanticValidator();

    private static final String VALID_RESOURCE = "challenge/semantic/valid";
    private static final String INVALID_RESOURCE = "challenge/semantic/invalid";


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
