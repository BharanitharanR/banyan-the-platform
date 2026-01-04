package com.banyan.platform.challenge;

import com.banyan.platform.runtime.EvidenceContext;

import java.util.HashMap;
import java.util.Map;

public final class CompiledChallenge {
    private final String challengeId;
    private final TaskNode task;
    private final int version;

    public CompiledChallenge(String challengeId,int version, TaskNode task){
        this.challengeId = challengeId;
        this.version = version;
        this.task = task;
    }

    public String getChallengeId() { return this.challengeId;}

    public int getVersion() { return this.version;}

    public Map<String, Boolean> evaluate(EvidenceContext context) {
        Map<String, Boolean> results = new HashMap<>();
        results.put(task.getTaskId(), task.evaluate(context));
        return results;
    }

}
