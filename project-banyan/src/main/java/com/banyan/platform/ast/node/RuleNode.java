package com.banyan.platform.ast.node;

import com.banyan.platform.runtime.EvidenceContext;

public interface RuleNode {
    public boolean evaluate(EvidenceContext context);
}
