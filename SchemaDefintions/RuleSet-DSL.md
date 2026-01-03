# Ruleset Definition DSL

**Project Banyan**

---

## Overview

A **Ruleset** defines **how multiple rules are composed** to produce a single logical outcome.

In Project Banyan, a Ruleset is **not a separate primitive**.
Instead, a Ruleset is a **composite Rule**, expressed using the same `Rule` envelope with a different execution mode.

> **A Ruleset is a composite rule whose evaluation result is derived from one or more child rules.**

Rulesets are designed to be:

* Purely declarative
* Deterministic
* Reusable across tasks and challenges
* Immutable once versioned
* Independent of users, persistence, or execution timing

Rulesets contain **no executable code** and are compiled into **AST nodes** by the ingestion compiler.

---

## Relationship to Rules and AST

Conceptually:

```
Rule (ATOMIC)     → Leaf AST node
Rule (COMPOSITE)  → Parent AST node (Ruleset)
```

At runtime, both atomic rules and rulesets are evaluated uniformly as nodes in a logic tree.

---

## Structure

```json
{
  "kind": "Rule",
  "id": "<string>",
  "version": <integer>,
  "status": "DRAFT | ACTIVE | DEPRECATED",
  "spec": {
    "mode": "COMPOSITE",
    "aggregation": "<aggregation>",
    "children": ["<rule-id>", "..."],
    "resultType": "<result-type>"
  }
}
```

---

## Field Definitions

### kind

* **Type:** string
* **Value:** Must be `"Rule"`
* **Purpose:** Identifies the definition as a Rule (atomic or composite)

---

### id

* **Type:** string
* **Constraints:**

    * Alphanumeric characters, underscores, hyphens
    * Must be globally unique
* **Purpose:** Canonical identifier for the ruleset

---

### version

* **Type:** integer
* **Constraints:**

    * Must be ≥ 1
    * Immutable once published
* **Purpose:** Enables versioned evolution without breaking historical evaluations

---

### status

* **Type:** enum
* **Allowed Values:** `DRAFT`, `ACTIVE`, `DEPRECATED`
* **Purpose:** Controls lifecycle, validation strictness, and runtime eligibility

---

### spec

Container for ruleset behavior parameters.

---

#### spec.mode

* **Type:** enum
* **Allowed Values:** `COMPOSITE`
* **Purpose:** Declares this rule as a composite rule (Ruleset)

---

#### spec.aggregation

* **Type:** enum
* **Allowed Values:**

    * `ALL` — logical AND
    * `ANY` — logical OR
* **Purpose:** Defines how child rule results are combined

---

#### spec.children

* **Type:** array of strings
* **Constraints:**

    * Must contain at least one entry
    * Each entry must reference a valid Rule ID
* **Purpose:** Ordered list of child rules evaluated by this ruleset

Child rules may be:

* Atomic rules
* Other composite rules (nested rulesets)

---

#### spec.resultType

* **Type:** enum
* **Allowed Values:** `BOOLEAN`, `NUMBER`, `SCORE`
* **Purpose:** Declares the output type of the ruleset evaluation

---

## Semantic Constraints (Compiler-Enforced)

The following constraints are enforced by the **Project Banyan ingestion compiler**, not by JSON Schema:

* All referenced child rules must exist
* Cyclic rule references are forbidden
* All child rules must produce compatible result types
* Aggregation strategy must be valid for the declared `resultType`
* Only `ACTIVE` rulesets may be compiled into the runtime AST
* Rulesets must be deterministic and side-effect free

---

## Example: Composite Ruleset

```json
{
  "kind": "Rule",
  "id": "safe_login_ruleset",
  "version": 1,
  "status": "ACTIVE",
  "spec": {
    "mode": "COMPOSITE",
    "aggregation": "ALL",
    "children": [
      "failed_attempts_rule",
      "business_hours_rule"
    ],
    "resultType": "BOOLEAN"
  }
}
```

**Meaning:**
A login attempt is considered safe if **all** child rules evaluate to true.

---

## Design Rationale

Rulesets are intentionally designed to:

* Express **composition**, not domain intent
* Remain agnostic of tasks, challenges, and users
* Compile cleanly into AST parent nodes
* Enable explanation and traceability through tree traversal

This design explicitly avoids:

* Embedded control flow
* Conditional scripting
* Side effects
* Runtime branching logic
* Domain-specific assumptions

---

## One Line to Remember

> **A Ruleset is a composite rule that defines how multiple rules are combined to produce a single outcome.**