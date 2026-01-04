# Rule Definition DSL (Project Banyan)

## Overview

A **Rule** defines **how evidence is evaluated** within Project Banyan.

Rules are the **smallest executable logic units** in the platform and are designed to be:

* Typed
* Parameterized
* Deterministic
* Reusable
* Immutable once versioned

Rules contain **no executable code**.
They are **pure metadata** and are evaluated by the Project Banyan rule engine at runtime.

---

## Structure

```json
{
  "kind": "Rule",
  "id": "<string>",
  "version": "<integer>",
  "status": "DRAFT | ACTIVE | DEPRECATED",
  "spec": {
    "mode": "ATOMIC",
    "type": "<string>",
    "input": "<string>",
    "operator": "<operator>",
    "value": "<literal>",
    "resultType": "BOOLEAN"
  }
}
```

---

## Field Definitions

### kind

* **Type:** string
* **Value:** Must be `"Rule"`
* **Purpose:** Identifies the definition as a Rule

---

### id

* **Type:** string
* **Constraints:**

  * Alphanumeric characters, underscores, hyphens
  * Must be globally unique
* **Purpose:** Canonical identifier for the rule

---

### version

* **Type:** integer
* **Constraints:**

  * Must be ≥ 1
  * Immutable once published
* **Purpose:** Enables versioned evolution of rules without breaking historical evaluations

---

### status

* **Type:** enum
* **Allowed Values:** `DRAFT`, `ACTIVE`, `DEPRECATED`
* **Purpose:** Controls lifecycle, validation strictness, and runtime eligibility

---

### spec

Container for rule behavior parameters.

---

#### spec.mode

* **Type:** enum
* **Allowed Values:** `ATOMIC`
* **Purpose:** Declares the rule as an atomic (non-composite) rule

> Composite logic is defined using **Rulesets**, not atomic rules.

---

#### spec.type

* **Type:** string
* **Purpose:** Declares the evaluation strategy used by the rule
* **Examples:**

  * `THRESHOLD`
  * `RANGE`
  * `DURATION`
  * `COUNT`

⚠️ Must correspond to a registered rule type in the **Rule Type Registry**.

---

#### spec.input

* **Type:** string
* **Purpose:** Name of the evidence metric or derived field evaluated by the rule
* **Examples:**

  * `speed_over_limit_seconds`
  * `lane_departure_count`

---

#### spec.operator

* **Type:** enum
* **Allowed Values:**

  * `<`
  * `<=`
  * `>`
  * `>=`
  * `==`
  * `!=`
* **Purpose:** Comparison operator applied during rule evaluation

---

#### spec.value

* **Type:** number | string | boolean
* **Purpose:** Literal value used for comparison during evaluation

---

#### spec.resultType

* **Type:** enum
* **Allowed Values:** `BOOLEAN`
* **Purpose:** Declares the output type of rule evaluation

---

## Semantic Constraints (Compiler-Enforced)

The following constraints are enforced by the **ingestion compiler**, not by JSON Schema:

* `type` must be registered in the Rule Type Registry
* `input` must exist in the Evidence Type registry
* `operator` must be valid for the given rule type
* `value` must be compatible with the input’s data type
* Rules must be deterministic and side-effect free
* Only `ACTIVE` rules may be compiled into the runtime AST

---

## Example: Threshold Rule

```json
{
  "kind": "Rule",
  "id": "speed_threshold_rule",
  "version": 1,
  "status": "ACTIVE",
  "spec": {
    "mode": "ATOMIC",
    "type": "THRESHOLD",
    "input": "speed_over_limit_seconds",
    "operator": "<=",
    "value": 10,
    "resultType": "BOOLEAN"
  }
}
```

**Meaning:**
The total time spent exceeding the speed limit must be less than or equal to 10 seconds.

---

## Design Rationale

Atomic rules are intentionally:

* Simple
* Typed
* Parameterized

This design explicitly avoids:

* Expression parsing
* Embedded logic
* Injection risks
* Non-deterministic behavior
* Debugging complexity

More complex logic is expressed through **Rulesets**, which compose atomic rules into executable logic trees.

---

## One Line to Remember

> **A Rule is a typed, deterministic predicate over evidence.**

