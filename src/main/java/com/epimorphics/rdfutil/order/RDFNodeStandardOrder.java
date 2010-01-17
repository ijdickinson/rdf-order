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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.RDFNode;

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
        // TODO Auto-generated method stub
        return 0;
    }

    /***********************************/
    /* Internal implementation methods */
    /***********************************/

    /***********************************/
    /* Inner class definitions         */
    /***********************************/

}

