package com.banyan.compiler.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class RuleSetSchemaValidator implements SchemaValidator{
    private static final ObjectMapper mapper = new ObjectMapper();
    private final JsonSchema schema;


    public RuleSetSchemaValidator() {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
        Optional<InputStream> schemaStream = Optional.ofNullable(getClass().getResourceAsStream("/schemas/ruleset.schema.json"));
        if(schemaStream.isPresent())
            this.schema = factory.getSchema(schemaStream.get());
        else
            throw new IllegalStateException(
                    "Ruleset schema not found on classpath");
    }

    @Override
    public List<String> validate(String json) {
        try
        {
            JsonNode dslJson = mapper.readTree(json);
            return this.schema.validate(dslJson).stream().map(ValidationMessage::getMessage).toList();
        }
        catch(Exception e)
        {
            return List.of( "RULE_SET_SCHEMA_PARSE_ERROR: " +
                    (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
        }

    }
}
