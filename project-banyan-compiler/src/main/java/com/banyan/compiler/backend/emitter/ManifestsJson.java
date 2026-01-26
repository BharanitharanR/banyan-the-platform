package com.banyan.compiler.backend.emitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ManifestsJson(ManifestHeader manifestHeader, List<String> fileLists) {
}
