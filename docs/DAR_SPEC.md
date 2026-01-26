# **DAR — Dropping Aerial Root**

## Banyan Compiler Output Specification (v1.0)

---

## 1. What is a DAR?

A **DAR (Dropping Aerial Root)** is a **self-contained compilation output** produced by the Banyan compiler.

It contains:

* Fully compiled artifacts
* Fully resolved dependencies
* Zero DSLs
* Zero compiler assumptions

A DAR is **portable, immutable, and runtime-ready**.

---

## 2. DAR Physical Format

A DAR is a **ZIP archive** with the following structure:

```
DAR.zip
├── MANIFEST.json
├── EvidenceType/<version>/<id>.json
├── Rule/<version>/<id>.json
├── Ruleset/<version>/<id>.json
├── Task/<version>/<id>.json
└── Challenge/<version>/<id>.json
```

This layout is **mandatory**.

---

## 3. MANIFEST.json (Locked)

### Purpose

The manifest is:

* An **index**
* A **listing**
* A **compiler stamp**

It is **not a policy document**.

---

### MANIFEST.json Structure

```json
{
  "manifestHeader": {
    "filename": "unique_task_challenge",
    "ver": "1",
    "timestmp": "1769428945260",
    "compVersion": "banyan-compiler-2.0"
  },
  "fileLists": [
    "Challenge/1/unique_task_challenge.json",
    "Task/1/task_with_actions.json",
    "Ruleset/1/login_ruleset.json",
    "Rule/1/max_failed_attempts.json",
    "EvidenceType/1/CONSOLIDATED_EVIDENCES.json"
  ]
}
```

---

### Manifest Rules

* `fileLists` **must list every artifact present**
* Paths **must match zip layout**
* No duplicates
* No implicit artifacts
* No dependency logic here

---

## 4. Compiled Artifact Contract (Uniform)

Every artifact in the DAR **must** conform to:

```json
{
  "id": "string",
  "version": number,
  "artifactType": "EvidenceType | Rule | Ruleset | Task | Challenge",
  "metadata": {
    "compilerVersion": "banyan-compiler-2.0",
    "compiledAtEpochMillis": number,
    "contentHash": "SHA256"
  },
  "dependencies": [
    {
      "type": "ArtifactType",
      "id": "string",
      "version": number
    }
  ],
  "payload": { }
}
```

---

## 5. Artifact Semantics (Compiler Guarantees)

* All dependencies exist in the DAR
* All versions are exact
* No circular dependencies (EvidenceType self-ref ignored)
* No runtime validation required
* Payload is final and executable

---

## 6. Compiler Responsibilities (Hard Boundary)

The compiler **must**:

* Validate schema
* Validate semantics
* Enforce compatibility
* Resolve dependencies
* Emit DAR only on success

The compiler **must not**:

* Emit partial DARs
* Emit runtime instructions
* Embed execution logic

---

## 7. Runtime Responsibilities (Deferred)

Runtime **only**:

* Loads MANIFEST.json
* Reads artifact files
* Resolves dependencies
* Executes Challenge

Runtime does **no validation**.

---

## 8. DAR as an Audit Artifact

A DAR is suitable for:

* AI decision audits
* Regulatory inspection
* Offline replay
* Forensic analysis

It is **model-agnostic** and **policy-explicit**.

---

## 9. Status

**DAR v1.0 is FINAL.**

Any future changes must be backward compatible.

---

## Final Architectural Statement

> Banyan does not ship rules.
> Banyan ships **decisions that can be proven**.

DAR is that proof.

