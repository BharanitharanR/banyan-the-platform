# PHASE2_PLAN.md

**Project Banyan – Phase 2 (Compiler & DSL)**

---

## Phase 2 Goal (Single Sentence, Non-Negotiable)

> **Transform DSL-defined challenges into serialized AST artifacts that can be executed by the existing runtime without any runtime code changes.**

If runtime code needs to change → **Phase 2 has failed**.

---

## Phase 2 Scope (What This Phase Does)

Phase 2 introduces **authoring-time capabilities**, not runtime behavior.

Specifically, Phase 2 delivers:

* DSL definitions (JSON)
* Structural validation
* Semantic validation
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

## High-Level Architecture

```
DSL (JSON)
  ↓
Schema Validation
  ↓
Semantic Validation
  ↓
AST Builder
  ↓
AST Serializer
  ↓
Serialized AST Artifact
```

Runtime consumes the artifact **as-is**.

---

## Module Structure (Recommended, Not Optional)

```
banyan-compiler/
├── dsl/
│   ├── task.schema.json
│   ├── rule.schema.json
│   ├── ruleset.schema.json
│   ├── challenge.schema.json
│   └── evidence.schema.json
│
├── compiler/
│   ├── SchemaValidator.java
│   ├── SemanticValidator.java
│   ├── AstBuilder.java
│   ├── CompilationError.java
│   └── CompilationResult.java
│
├── ast/
│   ├── (shared AST classes from Phase 1)
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

You will support **only what Phase 1 runtime can execute**.

* Task DSL
* Rule DSL
* Ruleset DSL
* Challenge DSL
* EvidenceType DSL

No optional fields unless absolutely required.

---

### 2️⃣ Schema Validation (Structural)

Purpose:

* Fail fast on malformed input
* Enforce required fields
* Enforce field types

Rules:

* JSON Schema only
* No custom logic here
* All errors must be explicit

---

### 3️⃣ Semantic Validation (Critical)

Purpose:

* Enforce *meaning*, not structure

Examples:

* Rule references must exist
* No cyclic rule references
* Task must reference valid ruleset
* Challenge must include ≥1 task
* ResultType compatibility

This is where most real systems fail — treat it seriously.

---

### 4️⃣ AST Builder (Core of Phase 2)

Responsibility:

* Transform validated DSL into **exact Phase-1 AST objects**

Rules:

* Reuse `RuleNode`, `TaskNode`, `CompiledChallenge`
* No new runtime abstractions
* No shortcuts

If you cannot feed the output directly into `RuntimeEvaluator`, it’s wrong.

---

### 5️⃣ AST Serialization

Purpose:

* Persist compiled challenges
* Enable runtime rehydration

Rules:

* Deterministic format (JSON or binary)
* Versioned
* No runtime logic inside serialization

---

### 6️⃣ CLI Compiler (Minimal)

Example usage:

```bash
banyan-compile \
  --input sample-challenge.json \
  --output sample-challenge.ast.json
```

Rules:

* No interactive prompts
* Fail loudly
* Human-readable errors

---

## Phase 2 Success Criteria (Binary)

Phase 2 is **DONE** only if all are true:

* [ ] DSL → AST compilation works
* [ ] Invalid DSL fails with clear errors
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

## Phase 2 Anti-Rabbit-Hole Rules

These are **mandatory**:

1. If runtime needs modification → stop and reassess
2. If DSL becomes “expressive” → stop
3. If you feel tempted to add “just one more feature” → park it
4. If error handling feels complex → simplify DSL

---

## What Phase 2 Proves (Career Impact)

Phase 2 demonstrates that you can:

* Design compile-time systems
* Separate authoring from execution
* Enforce semantics, not just syntax
* Preserve architectural boundaries
* Deliver evolvable platforms

This is **Staff / Principal-level skill**.

---

## What Comes After (Not Now)

Phase 3 may include:

* Runtime hydration
* Explainability
* Persistence
* Deployment models
* Scoring

But **none of that exists** until Phase 2 is DONE.

---

## Final Rule (Read This)

> **Phase 2 exists to protect Phase 1 from change.**

If Phase 1 remains untouched, Phase 2 succeeded.

