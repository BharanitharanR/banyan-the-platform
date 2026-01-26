package com.banyan.compiler.backend.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class CompiledArtifactSerializer extends StdSerializer<AbstractCompiledArtifact<?>> {

    public CompiledArtifactSerializer() {
        super(AbstractCompiledArtifact.class, false);
    }

    @Override
    public void serialize(AbstractCompiledArtifact<?> value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {

        // Start the Envelope
        gen.writeStartObject();

        // Basic Header Info
        gen.writeStringField("id", value.id());
        gen.writeNumberField("version", value.version());
        gen.writeStringField("artifactType", value.type().name());

        // Metadata (delegates to CompilationMetadata's own serialization)
        gen.writeObjectField("metadata", value.metadata());

        // Dependencies (List of references)
        gen.writeObjectField("dependencies", value.dependencies());

        // This is the most important part: gen.writeObject handles the generic T
        gen.writeFieldName("payload");
        gen.writeObject(value.payload());

        // End the Envelope
        gen.writeEndObject();
    }
}