package com.banyan.compiler.lint;

public final class LintFinding {
    private final String code;
    private final String message;
    public LintFinding(String code,String message){
        this.code = code;
        this.message = message;
    }

    public String getCode(){return this.code;}

    public String getMessage(){return this.message;}
}
