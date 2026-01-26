package com.banyan.compiler.backend.emitter;

import java.util.List;
import java.util.Map;

public record ManifestFileLists(Map<ManifestFileKey,String> fileLists) {
}
