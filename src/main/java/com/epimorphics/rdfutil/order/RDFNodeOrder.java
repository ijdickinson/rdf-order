/*****************************************************************************
 * File:    RDFNodeOrder.java
 * Project: rdf-order
 * Created: 14 Jan 2010
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

import com.hp.hpl.jena.rdf.model.*;

/**
 * <p>An ordering over RDF nodes (including both {@link Resource}s and {@link Literal}s).</p>
 *
 * @author Ian Dickinson, Epimorphics (mailto:ian@epimorphics.com)
 */
public interface RDFNodeOrder
    extends Comparator<RDFNode>
{
    /***********************************/
    /* Constants                       */
    /***********************************/

    /***********************************/
    /* External signature methods      */
    /***********************************/

}

