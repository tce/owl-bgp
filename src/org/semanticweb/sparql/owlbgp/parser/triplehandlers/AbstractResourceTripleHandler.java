package org.semanticweb.sparql.owlbgp.parser.triplehandlers;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;

public abstract class AbstractResourceTripleHandler {
    
    protected final TripleConsumer consumer;
    
    public AbstractResourceTripleHandler(TripleConsumer consumer) {
        this.consumer=consumer;
    }
//    public boolean isSubjectOrObjectAnonymous(Identifier subject, Identifier object) {
//        return consumer.isAnonymous(subject) || consumer.isAnonymous(object);
//    }
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object, boolean consume) {
        if (!consume) consumer.addTriple(subject, predicate, object);
    }
    public void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        handleTriple(subject, predicate, object, new HashSet<Annotation>());
    }
    public void handleTriple(Identifier subject, Identifier predicate, Identifier object, Set<Annotation> annotations) {
    }
//    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
//        return false;
//    }
//    public abstract boolean canHandle(Identifier subject, Identifier predicate, Identifier object);
//    public boolean consumeWhileStreaming() {
//        return false;
//    }
}