package com.banyan.platform.deserializer;

import com.fasterxml.jackson.databind.JsonNode;

public interface ArtifactDeserializer<T> {
    T deserialize(JsonNode node);
}
