package com.banyan.compiler.enums;

public enum EvidenceValueType {
               BOOLEAN,INTEGER,DECIMAL,STRING,DURATION;
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
