# Banyan Runtime

The **Banyan Runtime** executes compiled governance artifacts (DARs) deterministically. It evaluates
compiled challenges against supplied evidence and produces replayable results. This module is an
in-process Java library that focuses on **pure, deterministic evaluation** and **runtime isolation**
from DSL semantics (all interpretation happens in the compiler).

## Goals

- **DAR-only execution**: runtime consumes compiled artifacts, never raw DSL.
- **Determinism**: same DAR + evidence → same result every time.
- **Statelessness**: no persistence, no side effects.
- **Pure evaluation**: evidence is trusted and compiler-validated.

## What This Module Contains

| Area | Key Types | Responsibility |
| --- | --- | --- |
| DAR loading | `ZipDarLoader` | Reads compiled DAR archives into a `DarRuntimeContext`. |
| Runtime context | `DarRuntimeContext`, `DarRuntimeStore` | Immutable access to compiled challenges, rulesets, rules, and evidence types. |
| AST materialization | `AstBuilder` | Builds an executable AST from compiled rulesets. |
| Execution nodes | `ExecutableNode`, `LogicalExecutableNode`, `RuleExecutableNode` | Deterministic evaluation of logical and rule nodes. |
| Evidence handling | `EvidenceContext` | Provides evidence values during evaluation. |
| Error handling | `MissingEvidenceException`, `InvalidEvidenceTypeException` | Fail-fast runtime exceptions. |

## Architecture Overview

```
DAR (.dar) ──▶ ZipDarLoader ──▶ DarRuntimeContext
                           └──▶ AstBuilder ──▶ ExecutableNode tree
                                           └──▶ evaluate(EvidenceContext)
```

The runtime is designed to be **embeddable**, **testable**, and **replayable**. It never mutates state
and never attempts to infer semantics beyond what is encoded in the compiled artifact.

## Usage (Runtime Flow)

```java
var context = ZipDarLoader.load("/path/to/compilation_package.dar");
var astBuilder = new AstBuilder(context);

var rulesetKey = new DarRuntimeContext.RulesetKey(version, rulesetId);
ExecutableNode executable = astBuilder.build(rulesetKey);

EvidenceContext evidence = new EvidenceContext(Map.of(
    "failed_attempts", 2,
    "business_hours", true
));

boolean result = executable.evaluate(evidence);
```

> **Note:** Evidence is assumed to be compiler-validated. Missing or invalid evidence causes
> runtime exceptions such as `MissingEvidenceException`.

## Development

### Build

```bash
./mvnw -pl project-banyan-runtime -am clean package
```

### Test

```bash
./mvnw -pl project-banyan-runtime test
```

## References

- Runtime MVP design: [`docs/Banyan-runtime-MVP.md`](docs/Banyan-runtime-MVP.md)
- DAR specification: [`docs/DAR_SPEC.md`](../docs/DAR_SPEC.md)

## Non-Negotiable Invariants

1. DAR is the only executable input.
2. Evaluation is a pure function of `(DAR, ChallengeId, Evidence)`.
3. Execution is deterministic and stateless.
4. Runtime never interprets governance semantics.
