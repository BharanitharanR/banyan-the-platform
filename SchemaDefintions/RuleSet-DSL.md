# Ruleset Definition DSL

## Overview

A **Ruleset** defines **how individual Rules are composed** into a single logical expression.

A Ruleset does **not** introduce new evaluation logic.
It exists purely to **combine Rules deterministically** using boolean logic.

> **A Ruleset is a boolean expression tree over Rules.**

---

## Design Intent

Rulesets exist to:

* Compose simple rules into complex conditions
* Enable reuse of rules across multiple tasks
* Keep individual rules atomic and focused
* Preserve determinism and explainability

Rulesets are **compiled**, not interpreted at runtime.

---

## Core Characteristics

Rulesets are:

* Declarative
* Deterministic
* Immutable once versioned
* Purely compositional
* Reusable across Tasks and Challenges

Rulesets are **not executable by themselves**.

---

## What a Ruleset Can Do

✅ Reference existing Rules
✅ Combine Rules using logical operators
✅ Nest logical expressions arbitrarily
✅ Evaluate to a single boolean outcome

---

## What a Ruleset Cannot Do

❌ Define thresholds
❌ Define operators
❌ Access evidence directly
❌ Reference Tasks or Challenges
❌ Execute logic
❌ Perform cross-artifact resolution (Phase 3)

---

## JSON Structure

```json
{
  "kind": "Ruleset",
  "id": "<string>",
  "version": <integer>,
  "spec": {
    "expression": <expression>
  }
}
```

---

## Field Definitions

### kind

* **Type:** string
* **Value:** `"Ruleset"`
* **Purpose:** Identifies the DSL type

---

### id

* **Type:** string
* **Constraints:**

  * lowercase alphanumeric
  * underscores and hyphens allowed
  * globally unique within Rulesets
* **Purpose:** Canonical identifier

---

### version

* **Type:** integer
* **Constraints:**

  * must be ≥ 1
  * immutable once published
* **Purpose:** Enables safe evolution

---

### spec

Container for Ruleset behavior.

---

### spec.expression

Defines the logical structure of the Ruleset.

The expression is **recursive** and evaluates to a boolean.

---

## Expression Model

An expression can be **one of two types**:

### 1️⃣ Rule Reference Node

References an existing Rule by ID.

```json
{
  "ruleRef": "rule_id"
}
```

* Must reference a valid Rule (checked in Phase 3)
* Represents a leaf node in the expression tree

---

### 2️⃣ Logical Node

Combines expressions using boolean logic.

```json
{
  "operator": "AND",
  "operands": [ <expression>, <expression>, ... ]
}
```

#### operator

* **Allowed values:** `AND`, `OR`
* Determines how operands are combined

#### operands

* **Type:** array of expressions
* **Constraints:**

  * minimum 2 operands
* Each operand may itself be:

  * a ruleRef
  * another logical node

---

## Composite Rulesets

Rulesets support **structural composition** via nesting.

Example:

```json
{
  "operator": "OR",
  "operands": [
    {
      "operator": "AND",
      "operands": [
        { "ruleRef": "max_failed_attempts" },
        { "ruleRef": "within_business_hours" }
      ]
    },
    {
      "ruleRef": "ip_not_blacklisted"
    }
  ]
}
```

This forms a **single boolean expression tree**.

---

## Semantic Constraints (Compiler-Enforced)

These rules are enforced by the compiler, not JSON Schema:

* Expression must not be empty
* Logical nodes must have ≥ 2 operands
* Operators must be valid
* Ruleset must evaluate to a boolean
* No execution logic inside Ruleset
* No evidence access

⚠️ Cross-artifact validation (rule existence, cycles) is **deferred to Phase 3**.

---

## Evaluation Semantics

At runtime:

* Each `ruleRef` is evaluated independently
* Logical nodes combine results deterministically
* Evaluation order is defined by the AST
* No side effects are permitted

---

## Example Ruleset

```json
{
  "kind": "Ruleset",
  "id": "login_security_ruleset",
  "version": 1,
  "spec": {
    "expression": {
      "operator": "AND",
      "operands": [
        { "ruleRef": "max_failed_attempts" },
        { "ruleRef": "within_business_hours" }
      ]
    }
  }
}
```

**Meaning:**
A login attempt is valid only if:

* failed attempts are within limits
* the action occurs during business hours

---

## Design Rationale

Rulesets deliberately:

* Separate composition from logic
* Avoid cross-artifact coupling
* Preserve explainability
* Enable static compilation into ASTs

This design prevents:

* implicit dependencies
* hidden execution paths
* runtime surprises

---

## One Line to Remember

> **A Ruleset is a deterministic boolean expression over Rules.**

---

## Phase Boundary Note

Ruleset-to-Ruleset references (`rulesetRef`)
are **explicitly deferred to Phase 3**.

Phase 2 supports **structural composition only**.
