package com.banyan.compiler.backend.ruleset;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class CompiledRulesetSerializer extends StdSerializer<CompiledRuleset> {

    public CompiledRulesetSerializer() { this(null); }

    public CompiledRulesetSerializer(Class<CompiledRuleset> t) { super(t); }

    @Override
    public void serialize(CompiledRuleset value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {

        gen.writeStartObject();

        // The recursive tree starts here
        gen.writeFieldName("expression");
        gen.writeObject(value.root());

        gen.writeEndObject();
    }
}