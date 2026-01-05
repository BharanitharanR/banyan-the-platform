package com.banyan.compiler.testutil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public final class TestResourceLoader {

    public static List<String> loadJsonFiles(String resourcePath) {
        try {
            Path dir = Paths.get(
                    ClassLoader.getSystemResource(resourcePath).toURI()
            );

            return Files.list(dir)
                    .filter(p -> p.toString().endsWith(".json"))
                    .map(TestResourceLoader::readFile)
                    .collect(Collectors.toList());

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to load test resources", e);
        }
    }

    private static String readFile(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
