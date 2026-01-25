package com.banyan.compiler.backend.evidence;
import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.backend.context.CompilationContext;
import com.banyan.compiler.backend.spi.AbstractBackendCompiler;
import com.banyan.compiler.enums.ArtifactType;
import com.banyan.compiler.enums.EvidenceValueType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EvidenceBackendCompiler extends AbstractBackendCompiler<CompiledEvidenceTypeArtifact> {

    @Override
    public CompiledEvidenceTypeArtifact compile(
            JsonNode validatedDsl,
            CompilationContext context
    ) {
        String id = readId(validatedDsl);
        int version = readVersion(validatedDsl);
        CompilationMetadata metadata = metadata(validatedDsl);
        List<ArtifactReference> list = new ArrayList<>();

        Map<String, EvidenceField> fields = new LinkedHashMap<>();
        for (JsonNode f : validatedDsl.at("/spec/fields")) {
            fields.put(
                    f.get("name").asText(),
                    new EvidenceField(
                            f.get("name").asText(),
                            EvidenceValueType.valueOf(f.get("type").asText()),
                            f.get("required").asBoolean()
                    )
            );
        }

        CompiledEvidenceType compiledType =
                new CompiledEvidenceType(id, version, fields);
        list.add(new ArtifactReference(
                ArtifactType.EvidenceType,id,version
        ));
        return new CompiledEvidenceTypeArtifact(
                id, version, compiledType, metadata,list
        );
    }



}
