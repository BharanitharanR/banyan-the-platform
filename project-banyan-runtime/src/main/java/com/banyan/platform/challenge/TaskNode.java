package com.banyan.platform.challenge;

import com.banyan.platform.ast.node.RuleNode;
import com.banyan.platform.runtime.EvidenceContext;

public final class TaskNode {
    private final String taskId;
    private final RuleNode rule;

    public  TaskNode(String taskId, RuleNode rule){
        this.rule = rule;
        this.taskId = taskId;
    }

    public boolean evaluate(EvidenceContext ctx) {
        return this.rule.evaluate(ctx);
    }

    public String getTaskId() { return this.taskId;}
}
