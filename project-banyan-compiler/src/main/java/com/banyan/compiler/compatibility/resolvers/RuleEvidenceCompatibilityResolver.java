package com.banyan.compiler.compatibility.resolvers;

import com.banyan.compiler.compatibility.bootstrap.CompatibilityResolver;
import com.banyan.compiler.enums.EvidenceValueType;
import com.banyan.compiler.enums.RuleType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class RuleEvidenceCompatibilityResolver
        implements CompatibilityResolver<RuleType, EvidenceValueType> {

    private final Map<RuleType, Set<EvidenceValueType>> forward;
    private final Map<EvidenceValueType, Set<RuleType>> reverse;
private static final ObjectMapper mapper = new ObjectMapper();

    private RuleEvidenceCompatibilityResolver(
            Map<RuleType, Set<EvidenceValueType>> forward,
            Map<EvidenceValueType, Set<RuleType>> reverse
    ) {
        this.forward = forward;
        this.reverse = reverse;
    }

    public static RuleEvidenceCompatibilityResolver fromJson(String resource) {
        // parse JSON → forward map
        // build reverse map once
        JsonNode data = readJson(resource);
        assert data != null;
        Map<RuleType,Set<EvidenceValueType>> forward = new HashMap<>();
        Map<EvidenceValueType,Set<RuleType>> reverse = new HashMap<>();
        Set<RuleType> rType = new HashSet<>();
        Set<EvidenceValueType> evType = new HashSet<>();
        if(data.isObject())
        {
            data.fields().forEachRemaining(
                    map->{
                                        map.getValue().iterator().forEachRemaining(
                                                jsonNode1 -> {
                                                    evType.add(EvidenceValueType.valueOf(jsonNode1.asText()));
                                                    rType.add(RuleType.valueOf(map.getKey()));
                                                    if(reverse.containsKey(EvidenceValueType.valueOf(jsonNode1.asText()))) {
                                                        reverse.get(EvidenceValueType.valueOf(jsonNode1.asText())).iterator().forEachRemaining(
                                                            rType::add
                                                    );}
                                                    reverse.put(
                                                            EvidenceValueType.valueOf(jsonNode1.asText()),
                                                            Set.copyOf(rType)
                                                    );
                                                    rType.clear();
                                                }
                                        );
                        forward.put(RuleType.valueOf(map.getKey()),
                                Set.copyOf(evType)
                                );
                       evType.clear();

                    }
            );
        }
        return new RuleEvidenceCompatibilityResolver(forward,reverse);
    }

    @Override
    public boolean isCompatible(RuleType rule, EvidenceValueType evidence) {
        return forward.getOrDefault(rule, Set.of()).contains(evidence);
    }

    @Override
    public Set<EvidenceValueType> supportedRHS(RuleType rule) {
        return forward.getOrDefault(rule, Set.of());
    }

    @Override
    public Set<RuleType> supportedLHS(EvidenceValueType evidence) {
        return reverse.getOrDefault(evidence, Set.of());
    }

    public static JsonNode readJson(String resource)  {
        Optional<InputStream> schemaStream = Optional.ofNullable(RuleEvidenceCompatibilityResolver.class.getResourceAsStream(resource));
        if(schemaStream.isEmpty())
        {
            return null;
        }
            try {
                return  mapper.readTree(schemaStream.get().readAllBytes());
            }
            catch(IOException e)
            {
                throw  new RuntimeException("error"+e.getMessage());
            }
    }

    public static void main(String args[])
    {
        /*
        {
   "THRESHOLD": ["INTEGER", "DECIMAL", "DURATION"],
   "EQUALITY": ["BOOLEAN", "STRING", "INTEGER"],
   "RANGE": ["INTEGER", "DECIMAL", "DURATION"]
 }

         */
        RuleEvidenceCompatibilityResolver resolver =  RuleEvidenceCompatibilityResolver.fromJson("/compatibility/rule-evidence.json");
        System.out.println(resolver.isCompatible(RuleType.EQUALITY, EvidenceValueType.DURATION));
        System.out.println(resolver.isCompatible(RuleType.THRESHOLD, EvidenceValueType.DURATION));
        System.out.println(resolver.isCompatible(RuleType.RANGE, EvidenceValueType.BOOLEAN));
    }
}
