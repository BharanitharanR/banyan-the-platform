# PHASE1_PLAN.md

**Project Banyan – Phase 1**

---

## Phase 1 Goal (Non-Negotiable)

Build a **running runtime service** that can evaluate **one hardcoded challenge** containing **one task** backed by **one ruleset**, using **runtime-provided evidence**, and return a deterministic result.

This phase proves:

* The AST execution model works
* The runtime request → evaluation → response flow works
* The platform is executable, not conceptual

No extensibility, no plugins, no persistence.

---

## Explicit Non-Goals (Strict)

The following are **out of scope** for Phase 1:

* DSL ingestion
* JSON schema validation
* Compiler implementation
* Persistence (DB, files, etc.)
* Multiple challenges or tasks
* Explainability / tracing
* Metrics
* Visitors
* Optimization
* Configurability
* EvidenceType registry
* User management

If it is not required to evaluate **one challenge**, it is excluded.

---

## Fixed Scenario (Hardcoded)

### Challenge

* `challengeId`: `sample_challenge`
* `version`: `1`

### Task

* `taskId`: `sample_task`

### Ruleset Logic (Hardcoded AST)

> Task passes if:

* `failedAttempts <= 3`
* `businessHours == true`

This logic **must be represented as an AST**, not inline `if` statements.

---

## Runtime Input (Contract)

The runtime must accept:

```json
{
  "challengeId": "sample_challenge",
  "version": 1,
  "evidence": {
    "failedAttempts": 2,
    "businessHours": true
  }
}
```

No validation beyond null checks.

---

## Runtime Output (Contract)

The runtime must return:

```json
{
  "challengeId": "sample_challenge",
  "version": 1,
  "results": {
    "sample_task": {
      "passed": true
    }
  }
}
```

Exact shape may vary slightly, but:

* task-level result must exist
* boolean outcome must be visible

---

## Classes to Implement (Authoritative List)

You are **not allowed** to add more classes.

### AST Layer

```text
RuleNode                (interface)
AtomicRuleNode          (failedAttempts <= 3, businessHours)
CompositeRuleNode       (AND)
```

### Challenge Layer

```text
TaskNode
CompiledChallenge
```

### Runtime Layer

```text
EvidenceContext
ExecutionContext
RuntimeEvaluator
ChallengeAstRegistry
```

### API Layer

```text
RuntimeController
RuntimeRequest
RuntimeResponse
```

---

## Class Responsibilities (Brief, Enforced)

### RuleNode

* Represents executable logic
* Stateless
* Evaluates against EvidenceContext

### AtomicRuleNode

* One logical check
* No knowledge of tasks or challenges

### CompositeRuleNode

* Combines child RuleNodes
* Implements logical AND only

### TaskNode

* Binds a RuleNode to a taskId
* Produces a task-level result

### CompiledChallenge

* Root of execution
* Holds task(s)
* Orchestrates evaluation

### ChallengeAstRegistry

* Holds exactly **one** prebuilt CompiledChallenge
* No refresh, no hydration logic

### RuntimeEvaluator

* Orchestrates:

    * load AST
    * build ExecutionContext
    * evaluate challenge
    * return result

### RuntimeController

* Thin REST layer
* No logic

---

## Definition of “Done” (Binary)

Phase 1 is **DONE** only if **all** of the following are true:

* [ ] Runtime service starts successfully
* [ ] A POST request evaluates the hardcoded challenge
* [ ] AST objects are clearly visible in code (no inline logic)
* [ ] Evidence values affect outcome correctly
* [ ] Same AST is reused across requests
* [ ] Demo can be shown in < 10 minutes

If any checkbox is unchecked, Phase 1 is **not done**.

---

## Timebox

**Maximum allowed time:** 7 calendar days
If not done by then, scope is reduced further.

---

## Exit Criteria

Once Phase 1 is done:

* You may **not** refactor immediately
* You must **demo it to yourself**
* You must write `PHASE1_RETROSPECTIVE.md` (1 page max)

Only then do we proceed to Phase 2.

---

## Final Rule (Read This)

> **If a change does not help the demo succeed, it is forbidden in Phase 1.**
