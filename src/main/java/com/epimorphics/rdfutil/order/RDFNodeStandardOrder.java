/*****************************************************************************
 * File:    RDFNodeStandardOrder.java
 * Project: rdf-order
 * Created: 15 Jan 2010
 * By:      ian
 *
 * Copyright (c) 2010 Epimorphics Ltd. All rights reserved.
 *****************************************************************************/

// Package
///////////////

package com.epimorphics.rdfutil.order;


// Imports
///////////////

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.*;

/**
 * <p>Standard ordering for {@link RDFNode}s. The ordering is defined as follows:</p>
 * <ul>
 * <li>The ordering is a total ordering for all {@link RDFNode} values: resources, bNodes
 * and literals</li>
 * <li>Resources appear before literals</li>
 * <li>Anonymous resources (bNodes) appear before URI resources</li>
 * <li>Two bNodes are ordered according to a lexical comparison of their node ID's</li>
 * <li>Two URI resources are ordered according to a lexical comparison of their URI strings</li>
 * <li>Literals with datatypes appear before untyped literals</li>
 * <li>Two typed literals with different datatypes are ordered according to a lexical comparison
 * of their datatype URI strings</li>
 * <li>Two untyped literals are ordered according to the following rules:
 *   <ul>
 *   <li>literals are ordered according to a lexical comparison of the literal value</li>
 *   <li>in the case of two literals with identical literal values,
 *       a literal with a language tag will appear before a literal without a language tag, and
 *       if  literals with identical values both have lang tags, they will be ordered according
 *       to a lexical comparison of the lang-tag value</li>
 *   <li>
 *   </ul>
 * </li>
 * <li>Two typed literals with identical datatypes, where the datatype appears in the following list,
 *     are ordered by value (e.g for <code>xsd:int</code> values, smaller integers will appear earlier
 *     in the ordering than larger integers). For other datatypes not listed below, the values are ordered
 *     according to a lexical comparison of the string-form of the data value.
 *   <ul>
 *   <li>Boolean (i.e. <code>xsd:boolean</code>) &ndash; <code>false</code> precedes <code>true</code></li>
 *   <li>byte (i.e. <code>xsd:byte</code>)</li>
 *   <li>short integer (i.e. <code>xsd:short</code>)</li>
 *   <li>int (i.e. <code>xsd:int</code>)</li>
 *   <li>long (i.e. <code>xsd:long</code>)</li>
 *   <li>integer (i.e. <code>xsd:integer</code>)</li>
 *   <li>decimal (i.e. <code>xsd:decimal</code>)</li>
 *   <li>float (i.e. <code>xsd:float</code>)</li>
 *   <li>double (i.e. <code>xsd:double</code>)</li>
 *   <li>time (i.e. <code>xsd:time</code>)</li>
 *   <li>date (i.e. <code>xsd:date</code>)</li>
 *   <li>date-time (i.e. <code>xsd:dateTime</code>)</li>
 *   </ul>
 * </li>
 * <li>
 * </li>
 * </ul>
 *
 * @author Ian Dickinson, Epimorphics (mailto:ian@epimorphics.com)
 */
public class RDFNodeStandardOrder
    implements RDFNodeOrder
{
    /***********************************/
    /* Constants                       */
    /***********************************/

    /***********************************/
    /* Static variables                */
    /***********************************/

    @SuppressWarnings( value = "unused" )
    private static final Logger log = LoggerFactory.getLogger( RDFNodeStandardOrder.class );

    /***********************************/
    /* Instance variables              */
    /***********************************/

    /***********************************/
    /* Constructors                    */
    /***********************************/

    /***********************************/
    /* External signature methods      */
    /***********************************/

    @Override
    public int compare( RDFNode o1, RDFNode o2 ) {
        if (o1.isResource()) {
            if (o2.isResource()) {
                return compareResources( (Resource) o1, (Resource) o2 );
            }
            else {
                // o1 is a resource, o2 is a literal
                return -1;
            }
        }
        else {
            if (o2.isResource()) {
                // o2 is a resource, but o1 is a literal
                return 1;
            }
            else {
                return compareLiterals( (Literal) o1, (Literal) o2 );
            }
        }
    }

    /***********************************/
    /* Internal implementation methods */
    /***********************************/

    /**
     * Determine the ordering between two {@link Resource}s
     * @param r1 The first resource to compare
     * @param r2 The second resource to compare
     * @return less than zero if r1 precedes r2 in the order
     */
    protected int compareResources( Resource r1, Resource r2 ) {
        if (r1.isAnon()) {
            return r2.isAnon() ? compareAnonymousResources( r1, r2 ) : -1;
        }
        else {
            return r2.isAnon() ? 1 : compareURIResources( r1, r2 );
        }
    }

    /**
     * Determine the ordering between two named resources
     * @param r1
     * @param r2
     * @return An ordering based on the lexical comparison of the URI's of
     * r1 and r2
     */
    private int compareURIResources( Resource r1, Resource r2 ) {
        return r1.getURI().compareTo( r2.getURI() );
    }

    /**
     * Determine the ordering between two bNodes
     * @param r1
     * @param r2
     * @return An ordering based on the lexical comparison of the anonID's of
     * r1 and r2
     */
    protected int compareAnonymousResources( Resource r1, Resource r2 ) {
        return r1.getId().getLabelString().compareTo( r2.getId().getLabelString() );
    }

    /**
     * Determine the ordering between two arbitrary literals
     * @param l1
     * @param l2
     * @return Less than zero if l1 should precede l2 in the order
     */
    protected int compareLiterals( Literal l1, Literal l2 ) {
        if (l1.getDatatype() != null) {
            return (l2.getDatatype() != null) ? compareTypedLiterals( l1, l2 ) : -1;
        }
        else {
            return (l2.getDatatype() != null) ? 1 : compareUntypedLiterals( l1, l2 );
        }
    }

    /**
     * Determine the order between two untyped literals, which may or may not have
     * language tags
     *
     * @param l1 An untyped literal
     * @param l2 An untyped literal
     * @return Less than zero if l1 should precede l2 in the order
     */
    protected int compareUntypedLiterals( Literal l1, Literal l2 ) {
        int compare = l1.getLexicalForm().compareTo( l2.getLexicalForm() );
        if (compare != 0) {
            return compare;
        }
        else {
            // the literal values are the same, use lang tags to disambiguate
            if (hasLangTag( l1 )) {
                if (hasLangTag( l2 )) {
                    // both have lang tags
                    return l1.getLanguage().compareTo( l2.getLanguage() );
                }
                else {
                    return -1;
                }
            }
            else {
                return (hasLangTag( l2 )) ? 1 : 0;
            }
        }
    }

    /**
     * Return true if the given literal has a non-empty lang tag
     * @param l
     * @return True if the lang tag of <code>l</code> is non-null and
     * non-empty
     */
    protected boolean hasLangTag( Literal l ) {
        String t = l.getLanguage();
        return t != null && t.length() > 0;
    }


    /**
     * Determine the order between two typed literals, which may have different
     * or identical datatypes.
     * @param l1
     * @param l2
     * @return Less than zero if typed literal <code>l1</code> should precede
     * typed literal <code>l2</code> in the order
     */
    protected int compareTypedLiterals( Literal l1, Literal l2 ) {
        if (l1.getDatatype().equals( l2.getDatatype() )) {
            // types are the same
            return compareSameTypeLiterals( l1, l2 );
        }
        else {
            return l1.getDatatypeURI().compareTo( l2.getDatatypeURI() );
        }
    }

    /**
     * Determine the order between two typed literals, which are known to have
     * identical datatypes.
     * @param l1
     * @param l2
     * @return Less than zero if typed literal <code>l1</code> should precede
     * typed literal <code>l2</code> in the order
     */
    protected int compareSameTypeLiterals( Literal l1, Literal l2 ) {
        Object v1 = l1.getValue();
        Object v2 = l2.getValue();
        RDFDatatype d = l1.getDatatype();

        // certain well-known types we compare by value
        if (d.equals( XSDDatatype.XSDboolean )) {
            return ((Boolean) v1).compareTo( ((Boolean) v2 ) );
        }
        else if (d.equals( XSDDatatype.XSDbyte ) ||
                 d.equals( XSDDatatype.XSDshort ) ||
                 d.equals( XSDDatatype.XSDint ) ||
                 d.equals( XSDDatatype.XSDlong ) ||
                 d.equals( XSDDatatype.XSDinteger ) ||
                 d.equals( XSDDatatype.XSDdecimal )) {
            return compareNonFPNumbers( (Number) v1, (Number) v2 );
        }
        else if (d.equals( XSDDatatype.XSDdouble ) ||
                 d.equals( XSDDatatype.XSDfloat )) {
            return compareFPNumbers( (Number) v1, (Number) v2 );
        }
        else if (d.equals( XSDDatatype.XSDtime ) ||
                 d.equals( XSDDatatype.XSDdate ) ||
                 d.equals( XSDDatatype.XSDdateTime )) {
            return ((XSDDateTime) v1).compareTo( (XSDDateTime) v2 );
        }

        // default: compare lexical string images
        return l1.getLexicalForm().compareTo( l2.getLexicalForm() );
    }

    /**
     * Determine the order between two non-floating point numbers, which
     * may be integers, longs or big-decimals.
     * @param n1 A non-FP number object
     * @param n2 A non-FP number object
     * @return Less than one if n1 is less than n2
     */
    protected int compareNonFPNumbers( Number n1, Number n2 ) {
        if (n1 instanceof BigDecimal && n2 instanceof BigDecimal) {
            return ((BigDecimal) n1).compareTo( (BigDecimal) n2);
        }
        else if (n1 instanceof BigDecimal) {
            return ((BigDecimal) n1).compareTo( new BigDecimal( n2.longValue() ) );
        }
        else if (n2 instanceof BigDecimal) {
            return new BigDecimal( n1.longValue() ).compareTo( (BigDecimal) n2 );
        }
        else {
            long delta = n1.longValue() - n2.longValue();
            return (delta > Integer.MAX_VALUE) ? Integer.MAX_VALUE :
                            ((delta < Integer.MIN_VALUE) ? Integer.MIN_VALUE : (int) delta);
        }
    }

    /**
     * Determine the order between two floating point numbers, which
     * may be floats or doubles
     * @param n1 An FP number object
     * @param n2 An FP number object
     * @return Less than one if n1 is less than n2
     */
    protected int compareFPNumbers( Number n1, Number n2 ) {
        if (n1 instanceof Double) {
            if (n2 instanceof Double) {
                return ((Double) n1).compareTo( (Double) n2 );
            }
            else {
                return ((Double) n1).compareTo( new Double( n2.doubleValue() ));
            }
        }
        else if (n2 instanceof Double) {
            return new Double( n1.doubleValue() ).compareTo( (Double) n2 );
        }
        else {
            return ((Float) n1).compareTo( (Float) n2 );
        }
    }


    /***********************************/
    /* Inner class definitions         */
    /***********************************/

}

