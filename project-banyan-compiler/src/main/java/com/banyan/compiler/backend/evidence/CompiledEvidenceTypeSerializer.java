package com.banyan.compiler.backend.evidence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class CompiledEvidenceTypeSerializer extends StdSerializer<CompiledEvidenceType> {

    public CompiledEvidenceTypeSerializer() {
        this(null);
    }

    public CompiledEvidenceTypeSerializer(Class<CompiledEvidenceType> t) {
        super(t);
    }

    @Override
    public void serialize(CompiledEvidenceType value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {

        gen.writeStartObject();

        // Basic fields
        gen.writeStringField("id", value.id());
        gen.writeNumberField("version", value.version());



        // Fields Map
        gen.writeFieldName("fields");

        gen.writeStartArray();

        for (var entry : value.fields().entrySet()) {
            // gen.writeFieldName(entry.getKey());
            // This will use the default Record serialization for EvidenceField
            gen.writeObject(entry.getValue());
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}