# PHASE2_PLAN.md

**Project Banyan – Phase 2 (Compiler, DSL & Validation Pipeline)**

---

## Phase 2 Goal (Single Sentence, Non-Negotiable)

> **Transform DSL-defined challenges into serialized AST artifacts that can be executed by the existing runtime without any runtime code changes.**

If runtime code needs to change → **Phase 2 has failed**.

---

## Phase 2 Scope (What This Phase Does)

Phase 2 introduces **authoring-time capabilities**, not runtime behavior.

Specifically, Phase 2 delivers:

* DSL definitions (JSON)
* Structural validation (Schema)
* Semantic validation (Correctness)
* Linting (Quality & guidance)
* AST construction
* AST serialization
* CLI-based compilation

---

## Explicit Non-Goals (Hard Boundaries)

Phase 2 does **NOT** include:

* ❌ Runtime services (no Quarkus, no HTTP)
* ❌ Persistence layers (DBs, filesystems beyond output artifact)
* ❌ User management
* ❌ REST APIs
* ❌ Dashcam / ML / scoring logic
* ❌ UI / frontend
* ❌ Multi-tenant concerns
* ❌ Hot reload
* ❌ Caching strategies
* ❌ Version migration logic

If a task smells like “product”, it is **out of scope**.

---

## Architectural Principle (Locked)

> **Phase 2 is a compiler, not a service.**

* Deterministic input → deterministic output
* No long-running state
* No side effects beyond artifact generation

---

## High-Level Architecture (Revised & Locked)

```
DSL (JSON)
  ↓
Schema Validation      (Structure)
  ↓
Semantic Validation    (Correctness)
  ↓
Linting                (Quality & Guidance)
  ↓
AST Builder
  ↓
AST Serializer
  ↓
Serialized AST Artifact
```

* **Errors** block compilation
* **Lint warnings** do not block compilation (by default)
* Runtime consumes the artifact **as-is**

---

## Validation Stack – Responsibility Split (Authoritative)

### 1️⃣ Schema Validator — Structure

**Purpose**

* Enforce required fields
* Enforce field types
* Enforce enum constraints

**Characteristics**

* Mechanical
* Deterministic
* No domain interpretation

**Outcome**

* ❌ Schema errors → compilation fails

---

### 2️⃣ Semantic Validator — Correctness

**Purpose**

* Enforce logical invariants
* Prevent undefined runtime behavior

**Examples**

* Rule references must exist
* No cyclic rule dependencies
* Task must reference a valid ruleset
* Challenge must include ≥ 1 task
* ResultType compatibility between rules and rulesets

**Outcome**

* ❌ Semantic errors → compilation fails

> If runtime would not know what to do → it is a semantic error.

---

### 3️⃣ Linter — Quality & Guidance (First-Class Component)

**Purpose**

* Surface design smells
* Encourage best practices
* Guide authors without blocking them

**Examples**

* Deeply nested rulesets
* Unused evidence fields
* Duplicate rule semantics
* Overly broad rulesets
* Poor naming conventions
* Suspicious version gaps

**Characteristics**

* Advisory
* Non-blocking by default
* Evolvable over time

**Outcome**

* ⚠️ Lint findings → warnings only (configurable later)

> **Correctness must fail. Quality must guide.**

---

## Module Structure (Updated & Locked)

```
banyan-compiler/
├── dsl/
│   ├── task.schema.json
│   ├── rule.schema.json
│   ├── ruleset.schema.json
│   ├── challenge.schema.json
│   └── evidence.schema.json
│
├── schema/
│   └── JsonSchemaValidator.java
│
├── semantics/
│   └── SemanticValidator.java
│
├── lint/
│   ├── LintRule.java
│   ├── LintFinding.java
│   └── BanyanLinter.java
│
├── compiler/
│   └── AstBuilder.java
│
├── ast/
│   └── (shared AST classes from Phase 1)
│
├── serializer/
│   ├── AstSerializer.java
│   └── AstDeserializer.java
│
├── cli/
│   └── BanyanCompileCommand.java
│
└── examples/
    └── sample-challenge.json
```

---

## Phase 2 Deliverables (Concrete)

### 1️⃣ DSL Definitions (JSON)

Support **only what Phase 1 runtime can execute**:

* EvidenceType DSL
* Rule DSL
* Ruleset DSL
* Task DSL
* Challenge DSL

No optional fields unless absolutely required.

---

### 2️⃣ Compiler Pipeline

Compiler execution order is **fixed**:

1. Schema validation
2. Semantic validation
3. Linting
4. AST construction
5. AST serialization

Any deviation is a design error.

---

### 3️⃣ AST Builder (Core of Phase 2)

**Responsibility**

* Transform validated DSL into **exact Phase-1 AST objects**

**Rules**

* Reuse `RuleNode`, `TaskNode`, `CompiledChallenge`
* No new runtime abstractions
* No execution logic

If output cannot be fed directly into `RuntimeEvaluator`, it is wrong.

---

### 4️⃣ AST Serialization

**Purpose**

* Persist compiled challenges
* Enable runtime rehydration

**Rules**

* Deterministic format (JSON or binary)
* Explicit versioning
* No runtime logic embedded

---

### 5️⃣ CLI Compiler (Minimal)

Example usage:

```bash
banyan-compile \
  --input sample-challenge.json \
  --output sample-challenge.ast.json
```

**Rules**

* No interactive prompts
* Fail loudly on errors
* Human-readable error and lint output

---

## Phase 2 Success Criteria (Binary)

Phase 2 is **DONE** only if all are true:

* [ ] DSL → AST compilation works
* [ ] Schema errors fail clearly
* [ ] Semantic errors fail clearly
* [ ] Lint warnings are surfaced but non-blocking
* [ ] Serialized AST can be deserialized
* [ ] RuntimeEvaluator runs unchanged
* [ ] End-to-end demo works with DSL-defined challenge

No partial credit.

---

## Phase 2 Timebox

**Maximum: 3–4 weeks**

If not done by then:

* Scope is reduced
* Not extended

---

## Phase 2 Anti–Rabbit-Hole Rules (Mandatory)

1. If runtime needs modification → stop and reassess
2. If DSL becomes “expressive” → stop
3. If lint rules become mandatory correctness checks → stop
4. If error handling feels complex → simplify DSL

---

## What Phase 2 Proves (Career Impact)

Phase 2 demonstrates that you can:

* Design compiler pipelines
* Separate structure, correctness, and quality
* Enforce semantics without overreach
* Preserve architectural boundaries
* Build platforms others can safely extend

This is **Staff / Principal-level capability**.
