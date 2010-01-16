/*****************************************************************************
 * File:    RDFStatementOrder.java
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

import java.util.Comparator;

import com.hp.hpl.jena.rdf.model.Statement;

/**
 * <p>An ordering over RDF statements</p>
 *
 * @author Ian Dickinson, Epimorphics (mailto:ian@epimorphics.com)
 */
public interface RDFStatementOrder
    extends Comparator<Statement>
{
    /***********************************/
    /* Constants                       */
    /***********************************/

    /***********************************/
    /* External signature methods      */
    /***********************************/

}

