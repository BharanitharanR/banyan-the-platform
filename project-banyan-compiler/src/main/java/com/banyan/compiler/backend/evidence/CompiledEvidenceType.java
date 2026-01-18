package com.banyan.compiler.backend.evidence;

import com.banyan.compiler.backend.evidence.EvidenceField;
import com.banyan.compiler.enums.EvidenceValueType;

import java.util.Map;

public final class CompiledEvidenceType {

    private final String id;
    private final int version;
    private final Map<String, EvidenceField> fields;

    public CompiledEvidenceType(
            String id,
            int version,
            Map<String, EvidenceField> fields
    ) {
        this.id = id;
        this.version = version;
        this.fields = Map.copyOf(fields); // immutable
    }

    public String id() { return id; }
    public int version() { return version; }

    public boolean hasField(String name) {
        return fields.containsKey(name);
    }

    public EvidenceField field(String name) {
        return fields.get(name);
    }

    public EvidenceValueType fieldType(String name) {
        EvidenceField f = fields.get(name);
        return f != null ? f.type() : null;
    }

    public boolean isRequired(String name) {
        EvidenceField f = fields.get(name);
        return f != null && f.required();
    }

    public Map<String, EvidenceField> fields() {
        return fields;
    }
}
