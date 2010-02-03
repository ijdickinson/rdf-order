/*****************************************************************************
 * File:    RDFNodeStandardOrderTest.java
 * Project: rdf-order
 * Created: 16 Jan 2010
 * By:      ian
 *
 * Copyright (c) 2010 Epimorphics Ltd. All rights reserved.
 *****************************************************************************/

// Package
///////////////

package com.epimorphics.rdfutil.order;


// Imports
///////////////

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;

/**
 * <p>Unit tests for RDF node standard order</p>
 *
 * @author Ian Dickinson, Epimorphics (mailto:ian@epimorphics.com)
 */
public class RDFNodeStandardOrderTest
{
    /***********************************/
    /* Constants                       */
    /***********************************/

    /***********************************/
    /* Static variables                */
    /***********************************/

    @SuppressWarnings( value = "unused" )
    private static final Logger log = LoggerFactory.getLogger( RDFNodeStandardOrderTest.class );

    /***********************************/
    /* Instance variables              */
    /***********************************/

    private String NS = "http://example.com/rdf#";

    private Model m;
    private Resource r0, r1;
    private Resource b0, b1;
    private Literal l0, l1;

    private RDFNodeStandardOrder rnso = new RDFNodeStandardOrder();


    /***********************************/
    /* Constructors                    */
    /***********************************/

    /***********************************/
    /* External signature methods      */
    /***********************************/

    @Before
    public void setUp() throws Exception {
        m = ModelFactory.createDefaultModel();
        r0 = m.createResource( NS + "r0" );
        r1 = m.createResource( NS + "r1" );

        b0 = m.createResource();
        b1 = m.createResource();

        l0 = m.createLiteral( "l0" );
        l1 = m.createLiteral( "l1" );
    }

    @Test
    public void testCompare0() {
        assertTrue( "Bnode comes before named resource", rnso.compare( r0, b0 ) > 0 );
        assertTrue( "Bnode comes before named resource", rnso.compare( b0, r0 ) < 0 );
    }

    @Test
    public void testCompare1() {
        assertTrue( "Bnode comes before literal", rnso.compare( l0, b0 ) > 0 );
        assertTrue( "Bnode comes before literal", rnso.compare( b0, l0 ) < 0 );
    }

    @Test
    public void testCompare2() {
        assertTrue( "Named resource comes before literal", rnso.compare( l0, r0 ) > 0 );
        assertTrue( "Named resource comes before literal", rnso.compare( r0, l0 ) < 0 );
    }

    @Test
    public void testCompare3() {
        String id0 = b0.getId().getLabelString();
        String id1 = b1.getId().getLabelString();
        assertEquals( "Bnodes ordered according to anonID", id0.compareTo( id1 ), rnso.compare( b0, b1 ) );
        assertEquals( "Bnodes ordered according to anonID", id1.compareTo( id0 ), rnso.compare( b1, b0 ) );
    }

    @Test
    public void testCompare4() {
        String id0 = r0.getURI();
        String id1 = r1.getURI();
        assertEquals( "URI nodes ordered according to URI", id0.compareTo( id1 ), rnso.compare( r0, r1 ) );
        assertEquals( "URI nodes ordered according to URI", id1.compareTo( id0 ), rnso.compare( r1, r0 ) );
    }

    @Test
    public void testCompare5() {
        String lv0 = l0.getLexicalForm();
        String lv1 = l1.getLexicalForm();
        assertEquals( "Untyped literals are ordered lexically", lv0.compareTo( lv1 ), rnso.compare( l0, l1 ) );
        assertEquals( "Untyped literals are ordered lexically", lv1.compareTo( lv0 ), rnso.compare( l1, l0 ) );
    }

    @Test
    public void testCompare5a() {
        l0 = m.createLiteral( "foo", "en" );
        l1 = m.createLiteral( "bar" );
        assertTrue( "Literals are ordered lexically, even when lang tag present", rnso.compare( l0, l1 ) > 0 );
        assertTrue( "Literals are ordered lexically, even when lang tag present", rnso.compare( l1, l0 ) < 0 );
    }

    @Test
    public void testCompare6() {
        l0 = m.createLiteral( "foo", "en" );
        l1 = m.createLiteral( "foo" );
        assertTrue( "Literal with lang tag precedes literal without", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "Literal with lang tag precedes literal without", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare7() {
        l0 = m.createLiteral( "foo", "en" );
        l1 = m.createLiteral( "foo", "de" );
        assertTrue( "Literal with lang tags are ordered lexically by lang tag", rnso.compare( l0, l1 ) > 0 );
        assertTrue( "Literal with lang tags are ordered lexically by lang tag", rnso.compare( l1, l0 ) < 0 );
    }

    @Test
    public void testCompare8() {
        l0 = m.createLiteral( "1" );
        l1 = m.createTypedLiteral( 2 );
        assertTrue( "Literal with datatypes precede untyped literals", rnso.compare( l0, l1 ) > 0 );
        assertTrue( "Literal with datatypes precede untyped literals", rnso.compare( l1, l0 ) < 0 );
    }

    @Test
    public void testCompare9() {
        l0 = m.createTypedLiteral( 2, XSDDatatype.XSDinteger );
        l1 = m.createTypedLiteral( 2, XSDDatatype.XSDdecimal );
        String id0 = l0.getDatatypeURI();
        String id1 = l1.getDatatypeURI();
        assertEquals( "Literal with different datatypes are ordered by datatype URI", id0.compareTo( id1 ), rnso.compare( l0, l1 ) );
        assertEquals( "Literal with different datatypes are ordered by datatype URI", id1.compareTo( id0 ), rnso.compare( l1, l0 ) );
    }

    @Test
    public void testCompare10a() {
        l0 = m.createTypedLiteral( false );
        l1 = m.createTypedLiteral( true );
        assertTrue( "xsd:boolean false preceded xsd:boolean true", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:boolean false preceded xsd:boolean true", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10b() {
        byte v0 = 3;
        byte v1 = 20;
        l0 = m.createTypedLiteral( v0, XSDDatatype.XSDbyte );
        l1 = m.createTypedLiteral( v1, XSDDatatype.XSDbyte );
        assertTrue( "xsd:bytes are ordered by value", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:bytes are ordered by value", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10c() {
        int v0 = 3;
        int v1 = 20;
        l0 = m.createTypedLiteral( v0, XSDDatatype.XSDshort );
        l1 = m.createTypedLiteral( v1, XSDDatatype.XSDshort );
        assertTrue( "xsd:shorts are ordered by value", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:shorts are ordered by value", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10d() {
        int v0 = 3;
        int v1 = 20;
        l0 = m.createTypedLiteral( v0, XSDDatatype.XSDint );
        l1 = m.createTypedLiteral( v1, XSDDatatype.XSDint );
        assertTrue( "xsd:ints are ordered by value", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:ints are ordered by value", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10e() {
        long v0 = 3;
        long v1 = 20;
        l0 = m.createTypedLiteral( v0, XSDDatatype.XSDlong );
        l1 = m.createTypedLiteral( v1, XSDDatatype.XSDlong );
        assertTrue( "xsd:longs are ordered by value", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:longs are ordered by value", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10f() {
        long v0 = 3;
        long v1 = 200000000;
        l0 = m.createTypedLiteral( v0, XSDDatatype.XSDinteger );
        l1 = m.createTypedLiteral( v1, XSDDatatype.XSDinteger );
        assertTrue( "xsd:integers are ordered by value", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:integers are ordered by value", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10g() {
        long v0 = 3;
        long v1 = 20000000000L;
        l0 = m.createTypedLiteral( v0, XSDDatatype.XSDdecimal );
        l1 = m.createTypedLiteral( v1, XSDDatatype.XSDdecimal );
        assertTrue( "xsd:decimals are ordered by value", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:decimals are ordered by value", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10h() {
        float v0 = 3.0f;
        float v1 = 20.0f;
        l0 = m.createTypedLiteral( v0, XSDDatatype.XSDfloat );
        l1 = m.createTypedLiteral( v1, XSDDatatype.XSDfloat );
        assertTrue( "xsd:floats are ordered by value", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:floats are ordered by value", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10i() {
        double v0 = 3.0d;
        double v1 = 20.0d;
        l0 = m.createTypedLiteral( v0, XSDDatatype.XSDdouble );
        l1 = m.createTypedLiteral( v1, XSDDatatype.XSDdouble );
        assertTrue( "xsd:doubles are ordered by value", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:doubles are ordered by value", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10j() {
        l0 = m.createTypedLiteral( "12:34:56", XSDDatatype.XSDtime );
        l1 = m.createTypedLiteral( "01:23:45", XSDDatatype.XSDtime );
        assertTrue( "xsd:times are ordered by value", rnso.compare( l0, l1 ) > 0 );
        assertTrue( "xsd:times are ordered by value", rnso.compare( l1, l0 ) < 0 );
    }

    @Test
    public void testCompare10k() {
        l0 = m.createTypedLiteral( "2009-01-17", XSDDatatype.XSDdate );
        l1 = m.createTypedLiteral( "2009-01-18", XSDDatatype.XSDdate );
        assertTrue( "xsd:times are ordered by value", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:times are ordered by value", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10l() {
        l0 = m.createTypedLiteral( "2009-01-18T12:00:00", XSDDatatype.XSDdateTime );
        l1 = m.createTypedLiteral( "2009-01-18T13:00:00", XSDDatatype.XSDdateTime );
        assertTrue( "xsd:times are ordered by value", rnso.compare( l0, l1 ) < 0 );
        assertTrue( "xsd:times are ordered by value", rnso.compare( l1, l0 ) > 0 );
    }

    @Test
    public void testCompare10m() {
        l0 = m.createTypedLiteral( "arthur", XSDDatatype.XSDNCName);
        l1 = m.createTypedLiteral( "bedevere", XSDDatatype.XSDNCName);
        assertEquals( "other xsd values are ordered lexically by string image", l0.getLexicalForm().compareTo( l1.getLexicalForm() ), rnso.compare( l0, l1 ) );
        assertEquals( "other xsd values are ordered lexically by string image", l1.getLexicalForm().compareTo( l0.getLexicalForm() ), rnso.compare( l1, l0 ) );
    }



    /***********************************/
    /* Internal implementation methods */
    /***********************************/

    /***********************************/
    /* Inner class definitions         */
    /***********************************/

}

