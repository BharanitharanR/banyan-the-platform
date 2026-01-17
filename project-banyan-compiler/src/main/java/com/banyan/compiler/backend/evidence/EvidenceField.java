package com.banyan.compiler.backend.evidence;

import com.banyan.compiler.enums.EvidenceValueType;

public record EvidenceField(
        String name,
        EvidenceValueType type,
        boolean required
) {}
