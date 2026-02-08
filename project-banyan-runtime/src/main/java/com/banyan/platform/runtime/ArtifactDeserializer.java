package com.banyan.platform.runtime;

import com.fasterxml.jackson.databind.JsonNode;

public interface ArtifactDeserializer<T> {
    T deserialize(JsonNode node);
}
