package com.banyan.compiler.backend.outcome;

import com.banyan.compiler.backend.api.ArtifactReference;
import com.banyan.compiler.backend.api.CompilationErrorCode;
import com.banyan.compiler.backend.api.CompilationException;
import com.banyan.compiler.backend.api.CompiledArtifact;
import com.banyan.compiler.backend.context.CompilationContext;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CompilationOutcomeBuilder {
    private CompilationContext context;
    private final CompilationRoot root;
    public CompilationOutcomeBuilder(CompilationContext context, CompilationRoot root)
    {
        this.context = context;
        this.root = root;
    }

    public CompilationOutcome build()
    {
        // Freeze the context
        this.context.freeze();


        // 2. Collect errors from context (if you add error tracking)
        List<CompilationException> errors = new ArrayList<>();
        Set<CompiledArtifact<?>> reachable = new LinkedHashSet<>();
        try{
            CompiledArtifact<?> rootArtifact =  this.context.resolve(
                    this.root.type(),this.root.id(),this.root.version()
            );
            // Parse through the chain
            /*
                challenge -> Tasks
                Tasks -> rulesets
                Rulesets -> rules
                rules -> evidenceType
             */
            // 4. Reachability analysis

            try {
                walkGraph(rootArtifact, reachable, errors);
            } catch (Exception e) {
                errors.add(
                        new CompilationException(
                                CompilationErrorCode.LINKING_ERROR,
                                e.getMessage()
                        )
                );
            }

        }
        catch(CompilationException e)
        {
            errors.add(e);
            return failureOutcome(errors);
        }
        return new CompilationOutcome(root,reachable,errors);
    }

    private void walkGraph(CompiledArtifact<?>  artifact, Set<CompiledArtifact<?>> visited,List<CompilationException> errors)
    {
        if (!visited.add(artifact)) {
            return;
        }

        for (ArtifactReference ref : artifact.dependencies()) {
            try {
                CompiledArtifact<?> dep =
                        context.resolve(ref.type(), ref.id(), ref.version());
                walkGraph(dep, visited, errors);
            } catch (CompilationException e) {
                errors.add(e);
            }
        }
    }

    private CompilationOutcome failureOutcome(
            List<CompilationException> errors
    ) {
        return new CompilationOutcome(root, Set.of(), errors);
    }
}
