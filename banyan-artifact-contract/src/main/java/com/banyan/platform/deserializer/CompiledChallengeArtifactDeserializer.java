package com.banyan.platform.deserializer;

import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationMetadata;
import com.banyan.compiler.backend.challenge.CompiledChallenge;
import com.banyan.compiler.backend.challenge.CompiledChallengeArtifact;
import com.banyan.compiler.backend.challenge.CompiledTaskRef;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public final class CompiledChallengeArtifactDeserializer
        implements ArtifactDeserializer<CompiledChallengeArtifact> {

    private final ObjectMapper mapper;

    public CompiledChallengeArtifactDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CompiledChallengeArtifact deserialize(JsonNode node) {

        String id = node.get("id").asText();
        int version = node.get("version").asInt();
        // Runtime does NOT need full compilation metadata
        CompilationMetadata metadata = null; // or EMPTY
        JsonNode simplfiedPayload = node.get("payload");
        List<CompiledTaskRef> challenge = mapper.convertValue(
                simplfiedPayload.get("compiledTaskRefsList"),
                new TypeReference<List<CompiledTaskRef>>() {
                }
        );
        CompiledChallenge chg = new CompiledChallenge(challenge);
        List<ArtifactReference> deps =
                mapper.convertValue(
                        node.get("dependencies"),
                        new TypeReference<List<ArtifactReference>>() {}
                );

        return new CompiledChallengeArtifact(
                id,
                version,
                new CompiledChallenge(challenge),
                metadata,
                deps
        );
    }
}
