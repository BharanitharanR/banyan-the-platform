package com.banyan.platform.runtime;

import com.banyan.platform.challenge.CompiledChallenge;

public final class ChallengeAstRegistry {
    private final CompiledChallenge challenge;

    public ChallengeAstRegistry(CompiledChallenge challenge){
        this.challenge = challenge;
    }

    public CompiledChallenge get(String challengeId,int version){
        return this.challenge;
    }
}
