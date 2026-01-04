# 1️⃣ Phase-2 EvidenceType JSON Schema (PURE DATA)

This schema enforces **structure only**.
No operational semantics. No capabilities. No units.

Save as:

```
schemas/evidence-type.schema.json
```

```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "$id": "https://banyan.dev/schemas/evidence-type.schema.json",
  "title": "EvidenceTypeDefinition",
  "type": "object",
  "required": ["kind", "id", "version", "status", "spec"],
  "additionalProperties": false,

  "properties": {
    "kind": {
      "type": "string",
      "const": "EvidenceType",
      "description": "Identifies this definition as an EvidenceType"
    },

    "id": {
      "type": "string",
      "pattern": "^[A-Z][A-Z0-9_]{2,100}$",
      "description": "Globally unique identifier for the evidence type"
    },

    "version": {
      "type": "integer",
      "minimum": 1,
      "description": "Immutable version number of the evidence type"
    },

    "status": {
      "type": "string",
      "enum": ["DRAFT", "ACTIVE", "DEPRECATED"],
      "description": "Lifecycle status of the evidence type"
    },

    "spec": {
      "type": "object",
      "required": ["fields"],
      "additionalProperties": false,
      "properties": {
        "fields": {
          "type": "array",
          "minItems": 1,
          "items": {
            "$ref": "#/$defs/fieldDefinition"
          },
          "description": "List of evidence fields exposed by this evidence type"
        }
      }
    }
  },

  "$defs": {
    "fieldDefinition": {
      "type": "object",
      "required": ["name", "type", "required"],
      "additionalProperties": false,
      "properties": {
        "name": {
          "type": "string",
          "pattern": "^[a-zA-Z][a-zA-Z0-9_]{1,100}$",
          "description": "Field name used by rules as input"
        },

        "type": {
          "type": "string",
          "enum": ["BOOLEAN", "INTEGER", "DECIMAL", "STRING", "DURATION"],
          "description": "Primitive data type of the evidence field"
        },

        "required": {
          "type": "boolean",
          "description": "Whether this field must be present in runtime evidence"
        }
      }
    }
  }
}
```

---

# 2️⃣ EvidenceType DSL — Phase-2 Write-Up (LOCKED)

Save as:

```
docs/EVIDENCE_TYPE_DSL.md
```

---

## EvidenceType DSL (Phase 2)

### Overview

An **EvidenceType** defines the **structural contract** for evidence data supplied to the Banyan runtime.

It answers **one question only**:

> *What data fields exist, and what are their primitive types?*

EvidenceType deliberately **does not define**:

* how data is evaluated
* what operations are allowed
* how metrics are aggregated
* any domain-specific semantics

Those concerns belong to **Rules**, **Rulesets**, and **future capability layers**.

---

### Design Principles (Non-Negotiable)

* EvidenceType is **data-only**
* EvidenceType is **versioned**
* EvidenceType is **referenced, not executed**
* EvidenceType is **foundational and stable**

This strictness protects:

* compiler simplicity
* semantic clarity
* long-term evolvability

---

### Structure

```json
{
  "kind": "EvidenceType",
  "id": "LOGIN_ATTEMPT",
  "version": 1,
  "status": "ACTIVE",
  "spec": {
    "fields": [
      {
        "name": "failedAttempts",
        "type": "INTEGER",
        "required": true
      },
      {
        "name": "businessHours",
        "type": "BOOLEAN",
        "required": true
      }
    ]
  }
}
```

---

### Field Semantics

#### kind

* Must be `"EvidenceType"`
* Used for schema discrimination only

---

#### id

* Globally unique
* Stable across versions
* Referenced by Rule DSL

---

#### version

* Integer ≥ 1
* Immutable once published
* Enables historical replay and compatibility checks

---

#### status

* `DRAFT`: Allowed for authoring, may fail semantic validation
* `ACTIVE`: Eligible for compilation and runtime use
* `DEPRECATED`: Allowed for backward compatibility, discouraged for new rules

---

### spec.fields[]

Each field defines a **runtime fact**.

#### name

* Identifier used by rules as `input`
* Case-sensitive
* Must be unique within the EvidenceType (semantic validation)

#### type

Allowed primitive types (Phase 2):

* `BOOLEAN`
* `INTEGER`
* `DECIMAL`
* `STRING`
* `DURATION`

No custom or composite types in Phase 2.

#### required

* If `true`, runtime evidence **must** include this field
* Enforced by semantic validation, not schema

---

### Validation Responsibilities

#### Schema Validation

* Field presence
* Field types
* Enum correctness

#### Semantic Validation

* No duplicate field names
* EvidenceType ID uniqueness
* Rule inputs reference valid fields
* Required fields are honored

#### Linting (Non-Blocking)

* Unused fields
* Generic field names (`value`, `data`)
* Excessive number of fields
* Naming inconsistencies

---

### Explicit Non-Goals (Phase 2)

EvidenceType does **not** include:

* Units
* Capabilities
* Aggregation hints
* Temporal semantics
* Operator compatibility

These will be introduced **later**, in a separate capability layer.

---

### One Line to Remember

> **EvidenceType defines what exists — never what can be done.**

