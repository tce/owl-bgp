package org.semanticweb.sparql.owlbgp.model.axioms;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.AbstractExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.InterningManager;
import org.semanticweb.sparql.owlbgp.model.OWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class DisjointDataProperties extends AbstractAxiom implements ClassAxiom {
    private static final long serialVersionUID = -9059227935467720333L;

    protected static InterningManager<DisjointDataProperties> s_interningManager=new InterningManager<DisjointDataProperties>() {
        protected boolean equal(DisjointDataProperties object1,DisjointDataProperties object2) {
            if (object1.m_dataPropertyExpressions.size()!=object2.m_dataPropertyExpressions.size()
                    ||object1.m_annotations.size()!=object2.m_annotations.size())
                return false;
            for (DataPropertyExpression ope : object1.m_dataPropertyExpressions) {
                if (!contains(ope, object2.m_dataPropertyExpressions))
                    return false;
            } 
            for (Annotation anno : object1.m_annotations) {
                if (!contains(anno, object2.m_annotations))
                    return false;
            } 
            return true;
        }
        protected boolean contains(DataPropertyExpression dpe,Set<DataPropertyExpression> dpes) {
            for (DataPropertyExpression equiv: dpes)
                if (equiv==dpe)
                    return true;
            return false;
        }
        protected boolean contains(Annotation annotation,Set<Annotation> annotations) {
            for (Annotation anno : annotations)
                if (anno==annotation)
                    return true;
            return false;
        }
        protected int getHashCode(DisjointDataProperties object) {
            int hashCode=53;
            for (DataPropertyExpression equiv : object.m_dataPropertyExpressions)
                hashCode+=equiv.hashCode();
            for (Annotation anno : object.m_annotations)
                hashCode+=anno.hashCode();
            return hashCode;
        }
    };
    
    protected final Set<DataPropertyExpression> m_dataPropertyExpressions;
    
    protected DisjointDataProperties(Set<DataPropertyExpression> dataPropertyExpressions,Set<Annotation> annotations) {
        super(annotations);
        m_dataPropertyExpressions=Collections.unmodifiableSet(dataPropertyExpressions);//mutable
    }
    public Set<DataPropertyExpression> getDataPropertyExpressions() {
        return m_dataPropertyExpressions;
    }
    @Override
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("DisjointDataProperties(");
        writeAnnoations(buffer, prefixes);
        boolean notFirst=false;
        for (DataPropertyExpression equiv : m_dataPropertyExpressions) {
            if (notFirst)
                buffer.append(' ');
            else 
                notFirst=true;
            buffer.append(equiv.toString(prefixes));
        }
        buffer.append(")");
        return buffer.toString();
    }
    @Override
    public String toTurtleString(Prefixes prefixes, Identifier mainNode) {
        if (m_dataPropertyExpressions.size()==2) {
            return writeSingleMainTripleAxiom(prefixes, (Atomic)m_dataPropertyExpressions.iterator().next(), Vocabulary.OWL_DISJOINT_DATA_PROPERTIES, (Atomic)m_dataPropertyExpressions.iterator().next(), m_annotations);
        } else {
            StringBuffer buffer=new StringBuffer();
            Identifier bnode=AbstractExtendedOWLObject.getNextBlankNode();
            buffer.append(bnode);
            buffer.append(" ");
            buffer.append(Vocabulary.RDF_TYPE.toString(prefixes));
            buffer.append(" ");
            buffer.append(Vocabulary.OWL_ALL_DISJOINT_PROPERTIES.toString(prefixes));
            buffer.append(" . ");
            buffer.append(LB);
            Identifier listMainNode=AbstractExtendedOWLObject.getNextBlankNode();
            buffer.append(bnode);
            buffer.append(" ");
            buffer.append(Vocabulary.OWL_MEMBERS.toString(prefixes));
            buffer.append(" ");
            buffer.append(listMainNode);
            buffer.append(" . ");
            buffer.append(LB);
            DataPropertyExpression[] dataPropertyExpressions=m_dataPropertyExpressions.toArray(new DataPropertyExpression[0]);
            Identifier[] dataPropertyIDs=new Identifier[dataPropertyExpressions.length];
            for (int i=0;i<dataPropertyExpressions.length;i++) {
                dataPropertyIDs[i]=((Atomic)dataPropertyExpressions[i]).getIdentifier();
            }
            printSequence(buffer, prefixes, listMainNode, dataPropertyIDs);
            for (Annotation anno : m_annotations) 
                anno.toTurtleString(prefixes, bnode);
            return buffer.toString();
        } 
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static DisjointDataProperties create(Set<DataPropertyExpression> dataPropertyExpressions) {
        return create(dataPropertyExpressions,new HashSet<Annotation>());
    }
    public static DisjointDataProperties create(DataPropertyExpression... dataPropertyExpressions) {
        return create(new HashSet<DataPropertyExpression>(Arrays.asList(dataPropertyExpressions)),new HashSet<Annotation>());
    }
    public static DisjointDataProperties create(Set<DataPropertyExpression> dataPropertyExpressions,Set<Annotation> annotations) {
        return s_interningManager.intern(new DisjointDataProperties(dataPropertyExpressions,annotations));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        for (DataPropertyExpression ope : m_dataPropertyExpressions)
            variables.addAll(ope.getVariablesInSignature(varType));
        getAnnotationVariables(varType, variables);
        return variables;
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        Set<DataPropertyExpression> dataPropertyExpression=new HashSet<DataPropertyExpression>();
        for (DataPropertyExpression dpe : m_dataPropertyExpressions) {
            dataPropertyExpression.add((DataPropertyExpression)dpe.getBoundVersion(variablesToBindings));
        }
        return create(dataPropertyExpression, getBoundAnnotations(variablesToBindings));
    }
    public Axiom getAxiomWithoutAnnotations() {
        return create(m_dataPropertyExpressions);
    }
}