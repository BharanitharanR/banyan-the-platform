package com.banyan.compiler.enums;

public enum ArtifactType {
    EvidenceType,Rule,Ruleset,Challenge,Task;
    public static boolean contains(String value)
    {
        try {
            TaskActionOn.valueOf(value);
            return true;
        }
        catch(IllegalArgumentException e)
        {
            return false;
        }
    }
}
