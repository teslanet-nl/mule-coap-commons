/*******************************************************************************
 * Copyright (c) 2017, 2018 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/

package nl.teslanet.mule.transport.coap.commons.tests;


import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.teslanet.mule.transport.coap.commons.options.ETag;
import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


/**
 * Tests the implementation of the Etag class.
 * @author Rogier Cobben
 *
 */
public class ETagTest
{

    @Rule
    public ExpectedException exception= ExpectedException.none();

    @Test
    public void testConstructor() throws InvalidETagException
    {
        String etagValue1= "afb990";
        byte[] etagValue2= { (byte) 0xAF, (byte) 0xB9, (byte) 0x90 };

        ETag etag1= new ETag( etagValue1 );
        ETag etag2= new ETag( etagValue2 );

        assertTrue( "ETag contruction from String failed", etag1.toString().equals( etagValue1 ) );
        assertTrue( "ETag contruction from Byte[] failed", etag2.toString().equals( etagValue1 ) );
    }

    @Test
    public void testConstructorInvalidETagNullByteArray() throws Exception
    {
        byte[] etagValue1= null;
        exception.expect( InvalidETagException.class );
        exception.expectMessage( "null is not allowed" );
        ETag etag1= new ETag( etagValue1 );;
        etag1.equals( new Boolean( false ) );
        assertNotNull( etag1 );
    }

    @Test
    public void testConstructorInvalidETagEmptyByteArray() throws Exception
    {
        byte[] etagValue1= {};
        exception.expect( InvalidETagException.class );
        exception.expectMessage( "Given length is: 0" );
        ETag etag1= new ETag( etagValue1 );;
        etag1.equals( new Boolean( false ) );
        assertNotNull( etag1 );
    }

    @Test
    public void testConstructorInvalidETagLargeByteArray() throws Exception
    {
        byte[] etagValue1= new byte [9];
        for ( int i= 0; i < 9; i++ )
        {
            etagValue1[i]= (byte) i;
        }
        exception.expect( InvalidETagException.class );
        exception.expectMessage( "Given length is: 9" );
        ETag etag1= new ETag( etagValue1 );;
        etag1.equals( new Boolean( false ) );
        assertNotNull( etag1 );
    }

    @Test
    public void testConstructorInvalidETagEmptyString() throws Exception
    {
        String etagValue1= "";
        exception.expect( InvalidETagException.class );
        exception.expectMessage( "Given length is: 0" );
        ETag etag1= new ETag( etagValue1 );;
        etag1.equals( new Boolean( false ) );
        assertNotNull( etag1 );
    }

    @Test
    public void testConstructorInvalidETagUnevenString1() throws Exception
    {
        String etagValue1= "1";
        exception.expect( InvalidETagException.class );
        exception.expectMessage( "number found: 1" );
        ETag etag1= new ETag( etagValue1 );;
        etag1.equals( new Boolean( false ) );
        assertNotNull( etag1 );
    }

    @Test
    public void testConstructorInvalidETagUnevenString2() throws Exception
    {
        String etagValue1= "1122334455667";
        exception.expect( InvalidETagException.class );
        exception.expectMessage( "number found: 13" );
        ETag etag1= new ETag( etagValue1 );;
        etag1.equals( new Boolean( false ) );
        assertNotNull( etag1 );
    }

    @Test
    public void testConstructorInvalidETagLargeString() throws Exception
    {
        String etagValue1= "112233445566778899";
        exception.expect( InvalidETagException.class );
        exception.expectMessage( "Given length is: 9" );
        ETag etag1= new ETag( etagValue1 );;
        etag1.equals( new Boolean( false ) );
        assertNotNull( etag1 );
    }

    @Test
    public void testEquals() throws InvalidETagException
    {
        String etagValue1= "ffb990";
        byte[] etagValue2= { (byte) 0xFF, (byte) 0xB9, (byte) 0x90 };
        String etagValue3= "afb991";
        String etagValue4= "afb99100112233";
        String etagValue5= "00";

        ETag etag1= new ETag( etagValue1 );
        ETag etag2= new ETag( etagValue2 );
        ETag etag3= new ETag( etagValue3 );
        ETag etag4= new ETag( etagValue4 );
        ETag etag5= new ETag( etagValue5 );

        assertTrue( "ETag.equals failed to compare to equal etags", etag1.equals( etag1 ) );
        assertTrue( "ETag.equals failed to compare to equal etags", etag2.equals( etag2 ) );
        assertTrue( "ETag.equals failed to compare to equal etags", etag3.equals( etag3 ) );
        assertTrue( "ETag.equals failed to compare to equal etags", etag4.equals( etag4 ) );
        assertTrue( "ETag.equals failed to compare to equal etags", etag5.equals( etag5 ) );

        assertTrue( "ETag.equals failed to compare to equal etags", etag1.equals( new ETag( etagValue1 ) ) );
        assertTrue( "ETag.equals failed to compare to equal etags", etag2.equals( new ETag( etagValue2 ) ) );
        assertTrue( "ETag.equals failed to compare to equal etags", etag3.equals( new ETag( etagValue3 ) ) );
        assertTrue( "ETag.equals failed to compare to equal etags", etag4.equals( new ETag( etagValue4 ) ) );
        assertTrue( "ETag.equals failed to compare to equal etags", etag5.equals( new ETag( etagValue5 ) ) );

        assertTrue( "ETag.equals failed to compare to equal etags", etag1.equals( etag2 ) );
        assertTrue( "ETag.equals failed to compare to equal etags", etag2.equals( etag1 ) );

        assertFalse( "ETag.equals failed to compare to unequal etags", etag1.equals( etag3 ) );
        assertFalse( "ETag.equals failed to compare to unequal etags", etag3.equals( etag1 ) );
        assertFalse( "ETag.equals failed to compare to unequal etags", etag1.equals( etag4 ) );
        assertFalse( "ETag.equals failed to compare to unequal etags", etag4.equals( etag1 ) );
        assertFalse( "ETag.equals failed to compare to unequal etags", etag5.equals( etag2 ) );
    }

    @Test
    public void testCompareTo() throws InvalidETagException
    {
        String etagValue1= "00";
        String etagValue2= "afb990";
        byte[] etagValue3= { (byte) 0xAF, (byte) 0xB9, (byte) 0x90 };
        String etagValue4= "afb991";
        String etagValue5= "afb99100112233";

        ETag etag1= new ETag( etagValue1 );
        ETag etag2= new ETag( etagValue2 );
        ETag etag3= new ETag( etagValue3 );
        ETag etag4= new ETag( etagValue4 );
        ETag etag5= new ETag( etagValue5 );

        assertEquals( "ETag.compareTo failed to compare to equal etags", etag1.compareTo( etag1 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag2.compareTo( etag2 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag3.compareTo( etag3 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag4.compareTo( etag4 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag5.compareTo( etag5 ), 0 );

        assertEquals( "ETag.compareTo failed to compare to equal etags", etag1.compareTo( etag5 ), -1 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag2.compareTo( etag1 ), 1 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag3.compareTo( etag2 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag4.compareTo( etag3 ), 1 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag5.compareTo( etag4 ), 1 );

        assertEquals( "ETag.compareTo failed to compare to equal etags", etag1.compareTo( etag2 ), -1 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag2.compareTo( etag3 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag3.compareTo( etag4 ), -1 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag4.compareTo( etag5 ), -1 );
        assertEquals( "ETag.compareTo failed to compare to equal etags", etag5.compareTo( etag1 ), 1 );

    }

    @Test
    public void testEqualsToNull() throws Exception
    {
        String etagValue1= "1122334455667788";
        ETag etag1= new ETag( etagValue1 );;
        exception.expect( NullPointerException.class );
        etag1.equals( null );
        assertNotNull( etag1 );
    }

    @Test
    public void testCompareToNull() throws Exception
    {
        String etagValue1= "1122334455667788";
        ETag etag1= new ETag( etagValue1 );;
        exception.expect( NullPointerException.class );
        etag1.compareTo( null );
        assertNotNull( etag1 );
    }

    @Test
    public void testEqualsToWrongClass() throws Exception
    {
        String etagValue1= "1122334455667788";
        ETag etag1= new ETag( etagValue1 );;
        exception.expect( ClassCastException.class );
        etag1.equals( new Boolean( false ) );
        assertNotNull( etag1 );
    }

    @Test
    public void testAsBytes() throws InvalidETagException
    {
        byte[] etagValue1= { (byte) 0x00 };
        String etagValue2= "afb990";
        byte[] etagValue3= { (byte) 0xAF, (byte) 0xB9, (byte) 0x90 };

        ETag etag1= new ETag( "00" );
        ETag etag2= new ETag( etagValue2 );
        ETag etag3= new ETag( etagValue3 );

        assertArrayEquals( "ETag.asBytes gives wrong value", etag1.asBytes(), etagValue1 );
        assertArrayEquals( "ETag.asBytes gives wrong value", etag2.asBytes(), etagValue3 );
        assertArrayEquals( "ETag.asBytes gives wrong value", etag3.asBytes(), etagValue3 );
    }

    @Test
    public void testToString() throws InvalidETagException
    {
        byte[] etagValue1= { (byte) 0x00 };
        String etagValue2= "afb990";
        byte[] etagValue3= { (byte) 0xAF, (byte) 0xB9, (byte) 0x90 };

        ETag etag1= new ETag( etagValue1 );
        ETag etag2= new ETag( etagValue2 );
        ETag etag3= new ETag( etagValue3 );

        assertTrue( "ETag.toString gives wrong value", etag1.toString().equals( "00" ) );
        assertTrue( "ETag.toString gives wrong value", etag2.toString().equals( etagValue2 ) );
        assertTrue( "ETag.toString gives wrong value", etag3.toString().equals( etagValue2 ) );
    }
}
