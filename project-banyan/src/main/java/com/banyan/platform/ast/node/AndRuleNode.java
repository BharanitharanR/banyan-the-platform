package com.banyan.platform.ast.node;

import com.banyan.platform.runtime.EvidenceContext;

public class AndRuleNode implements RuleNode{

    private final RuleNode leftRuleNode;
    private final RuleNode rightRuleNode;

    public AndRuleNode(RuleNode leftRuleNode,RuleNode rightRuleNode) {
        this.leftRuleNode  = leftRuleNode;
        this.rightRuleNode = rightRuleNode;
    }
    @Override
    public boolean evaluate(EvidenceContext context) {
        return leftRuleNode.evaluate(context) && rightRuleNode.evaluate(context);
    }
}
