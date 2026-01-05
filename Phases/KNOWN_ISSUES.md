# KNOWN_ISSUES.md

This document tracks **known limitations, library quirks, and deferred concerns** in Project Banyan.
All items listed here are **intentional trade-offs**, not unacknowledged bugs.

---

## 1. EvidenceType Enum Validation in JSON Schema

**Status:** Open
**Severity:** Low
**Phase Affected:** Phase 2 (Compiler – Schema Validation)

---

### Description

When validating `EvidenceType` DSL definitions using
`networknt-json-schema-validator` (Draft 2020-12), enum constraints defined inside `$defs` and referenced via array `items` are **not consistently enforced**.

Example:

```json
{
  "name": "x",
  "type": "FLOAT",
  "required": true
}
```

Should fail against:

```json
"type": {
  "type": "string",
  "enum": ["BOOLEAN", "INTEGER", "DECIMAL", "STRING", "DURATION"]
}
```

But schema validation may pass unexpectedly.

---

### Root Cause

This behavior appears to be a limitation or quirk of the `networknt-json-schema-validator` library when:

* Using Draft 2020-12
* Resolving `$defs` via `$ref`
* Validating array `items`
* Loading schemas from classpath resources

Despite correct schema structure and validator configuration, enum evaluation inside referenced definitions is unreliable.

---

### Impact

* Structural validation works as expected for:

    * required fields
    * object structure
    * array cardinality
    * additionalProperties constraints
* Enum membership for `fieldDefinition.type` is **not reliably enforced** at schema level

No runtime impact.

---

### Mitigation (Chosen Approach)

Enum enforcement for `EvidenceType.spec.fields[].type` is treated as a **semantic validation concern** rather than a schema validation concern.

Specifically:

* Schema validation enforces **shape**
* Semantic validation enforces **meaning**
* Allowed field types are validated in `EvidenceTypeSemanticValidator`

This approach:

* Avoids reliance on fragile library behavior
* Preserves compiler determinism
* Aligns with Banyan’s architectural separation of concerns

---

### Deferred Actions

This issue may be revisited in the future by:

* Upgrading `networknt-json-schema-validator`
* Evaluating alternative JSON Schema libraries
* Re-introducing enum enforcement at schema level if reliable support is confirmed

No action is required for Phase 2 completion.

---

### Decision Record

> Correctness is enforced at the semantic layer where behavior is deterministic.
> Schema validation remains strictly structural.
