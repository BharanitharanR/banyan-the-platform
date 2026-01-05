package com.banyan.compiler.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;

import java.io.InputStream;
import java.rmi.UnexpectedException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EvidenceTypeSchemaValidator implements SchemaValidator {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final JsonSchema schema;

    public EvidenceTypeSchemaValidator(){
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
        Optional<InputStream> schemaStream = Optional.ofNullable(getClass().getResourceAsStream("/schemas/evidence-type.schema.json"));
        if(schemaStream.isPresent())
             this.schema = factory.getSchema(schemaStream.get());
        else
            throw new IllegalStateException(
                    "EvidenceType schema not found on classpath");

    }

    @Override
    public List<String> validate(String json) {
        try {

            JsonNode node = mapper.readTree(json);
            Set<ValidationMessage> errors = this.schema.validate(node);
            return errors.stream().map(ValidationMessage::getMessage).collect(Collectors.toList());
        }
        catch(Exception e)
        {
            return List.of("Invalid JSON input: " + e.getMessage());
        }
    }
}
