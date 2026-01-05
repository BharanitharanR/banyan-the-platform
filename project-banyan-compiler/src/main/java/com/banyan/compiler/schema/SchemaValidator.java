package com.banyan.compiler.schema;

import java.util.List;

public interface SchemaValidator {
    List<String> validate(String json);
}

