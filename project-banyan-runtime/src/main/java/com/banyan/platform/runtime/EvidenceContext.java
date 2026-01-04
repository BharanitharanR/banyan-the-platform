package com.banyan.platform.runtime;
// For phase 1 a strict decisoin to avoid Lomboks. Results over anything
// import lombok.Getter;
// import lombok.Setter;


import com.banyan.platform.ast.node.AndRuleNode;
import com.banyan.platform.ast.node.BusinessHoursRuleNode;
import com.banyan.platform.ast.node.FailedAttemptsRuleNode;

public class EvidenceContext {
    private final int failedAttempts;
    private final boolean businessHours;

    public EvidenceContext(int failedAttempts,boolean businessHours) {
        this.businessHours = businessHours;
        this.failedAttempts = failedAttempts;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public boolean isBusinessHours() {
        return businessHours;
    }

    public static void main(String args[])
    {
        EvidenceContext ctx = new EvidenceContext(4,true);
        BusinessHoursRuleNode br = new BusinessHoursRuleNode();
        FailedAttemptsRuleNode failed = new FailedAttemptsRuleNode();
        AndRuleNode and = new AndRuleNode(br,failed);
        System.out.println(and.evaluate(ctx)+":"+br.evaluate(ctx));
    }
}

