# Compatibility Matrix (Compiler Policy Layer)

## Overview

The **Compatibility Matrix** is a *compiler-owned policy layer* that defines which **Rule Types** are allowed to operate on which **Evidence Value Types**.

It exists **only in the compiler backend** and is enforced **at compilation time**, not at runtime.

This design intentionally decouples:

* Rule semantics
* Evidence schemas
* Runtime evaluation logic

---

## Problem This Solves

In Banyan, **Rules** and **Evidence Types** are authored independently.

Example:

* A rule may be of type `THRESHOLD`
* An evidence field may be of type `BOOLEAN`

Without an explicit compatibility policy:

* The compiler cannot decide if the rule is valid
* Runtime errors become inevitable
* DSL evolution becomes tightly coupled

Embedding compatibility inside:

* Rule DSL ❌
* Evidence DSL ❌
* Runtime ❌

…creates long-term rigidity.

---

## Solution: Compiler Compatibility Matrix

The compiler maintains a **directional compatibility policy**:

> *Which Evidence Value Types are allowed for a given Rule Type*

This policy is **authoritative**, **centralized**, and **version-controlled**.

Example policy:

```json
{
  "THRESHOLD": ["INTEGER", "DECIMAL", "DURATION"],
  "EQUALITY":  ["BOOLEAN", "STRING", "INTEGER"],
  "RANGE":     ["INTEGER", "DECIMAL", "DURATION"]
}
```

---

## Directionality (Important)

The matrix is **rule-driven**, not evidence-driven.

That is:

* Rules decide what they can operate on
* Evidence types remain passive

This avoids:

* Evidence DSL knowing about rules
* Bidirectional coupling
* Semantic leakage into runtime

---

## Design Pattern Used

### **Policy Index + Compatibility Resolver Pattern**

The compatibility system is implemented as:

1. **Static policy source** (JSON / config / artifact)
2. **Indexed at compiler startup**
3. **Queried during compilation**

This is *not*:

* A registry pattern
* A service locator
* A plugin framework

It is a **compiler kernel policy layer**.

---

## Core Interface

```java
public interface CompatibilityResolver<A, B> {

    boolean isCompatible(A left, B right);

    Set<B> supportedRight(A left);

    Set<A> supportedLeft(B right);
}
```

### What this enables

| Capability       | Description                              |
| ---------------- | ---------------------------------------- |
| One-to-one check | Validate a specific rule/evidence pair   |
| Forward lookup   | What evidence types a rule supports      |
| Reverse lookup   | What rules can apply to an evidence type |

---

## Compiler Bootstrap Integration

Resolvers are registered **once** at compiler startup:

```java
bootstrap.addCompatibilityResolver(
    RuleType.class,
    EvidenceValueType.class,
    ruleEvidenceResolver
);

bootstrap.freeze();
```

After freezing:

* No new policies can be added
* The compiler operates in a deterministic mode

---

## Compilation-Time Enforcement

During backend compilation:

```java
CompatibilityResolver<RuleType, EvidenceValueType> resolver =
    context.compatibility(RuleType.class, EvidenceValueType.class);

if (!resolver.isCompatible(ruleType, evidenceType)) {
    throw new CompilationException(
        "RULE_EVIDENCE_INCOMPATIBLE"
    );
}
```

### Result

* Invalid DSLs fail early
* Compiled artifacts are always type-safe
* Runtime never needs to validate compatibility

---

## Why This Is **Not** an AST

Although it resembles type checking, this is **not a runtime AST**.

Key differences:

| Runtime AST        | Compatibility Matrix   |
| ------------------ | ---------------------- |
| Evaluates data     | Validates policy       |
| Loaded per request | Loaded once at startup |
| Dynamic            | Immutable after freeze |
| Business logic     | Compiler logic         |

This is closer to a **static type compatibility table** used by compilers.

---

## Benefits

### ✔ Decoupling

Rules and Evidence evolve independently.

### ✔ Deterministic Compilation

Same input → same output → same failures.

### ✔ Runtime Simplification

Runtime assumes correctness.

### ✔ Extensibility

New rule types can be added without touching DSLs.

---

## What This Is Not

* ❌ Not schema validation
* ❌ Not semantic validation
* ❌ Not runtime enforcement
* ❌ Not a registry framework

It is a **compiler policy layer**.

---

## Summary

The Compatibility Matrix:

* Encodes rule–evidence compatibility
* Lives exclusively in the compiler backend
* Is loaded and indexed at startup
* Enforces correctness before runtime
* Enables scalable DSL evolution

This is a **deliberate compiler design choice**, not an implementation detail.
