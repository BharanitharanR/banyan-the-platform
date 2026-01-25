package com.banyan.compiler.enums;

public enum CompilationMode {
    SELF_CONTAINED,   // MVP 1
    LINKED ;           // Future
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
