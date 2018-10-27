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

import java.util.LinkedList;
import java.util.List;

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
        String etagValue3= "FF";

        ETag etag1= new ETag( etagValue1 );
        ETag etag2= new ETag( etagValue2 );
        ETag etag3= new ETag( etagValue3 );

        assertTrue( "ETag contruction from String failed", etag1.toString().equals( etagValue1 ) );
        assertTrue( "ETag contruction from Byte[] failed", etag2.toString().equals( etagValue1 ) );
        assertTrue( "ETag contruction from Byte[] failed", etag3.toString().equals( "ff" ) );
    }
    
    @Test
    public void testCreate() throws InvalidETagException
    {
        String etagValue1= "afb990";
        byte[] etagValue2= { (byte) 0xAF, (byte) 0xB9, (byte) 0x90 };
        String etagValue3= "FF";

        ETag etag1= ETag.createFromHexString( etagValue1 );
        ETag etag2= ETag.create( etagValue2 );
        ETag etag3= ETag.createFromHexString( etagValue3 );

        assertTrue( "ETag contruction from String failed", etag1.toString().equals( etagValue1 ) );
        assertTrue( "ETag contruction from Byte[] failed", etag2.toString().equals( etagValue1 ) );
        assertTrue( "ETag contruction from Byte[] failed", etag3.toString().equals( "ff" ) );
    }

    @Test
    public void testConstructorInvalidETagNullByteArray() throws Exception
    {
        byte[] etagValue1= null;
        exception.expect( InvalidETagException.class );
        exception.expectMessage( "null is not allowed" );
        ETag etag1= new ETag( etagValue1 );
        etag1.equals( new Boolean( false ) );
        assertNotNull( etag1 );
    }

    @Test
    public void testCreateInvalidETagNullByteArray() throws Exception
    {
        byte[] etagValue1= null;
        exception.expect( InvalidETagException.class );
        exception.expectMessage( "null is not allowed" );
        ETag etag1= ETag.create( etagValue1 );
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
    public void testCreateInvalidETagEmptyByteArray() throws Exception
    {
        byte[] etagValue1= {};
        exception.expect( InvalidETagException.class );
        exception.expectMessage( "Given length is: 0" );
        ETag etag1= ETag.create( etagValue1 );;
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
        assertNotEquals( (Object)etag1.asBytes(), (Object)etagValue1 );
        assertNotEquals( (Object)etag3.asBytes(), (Object)etagValue3 );
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

        assertTrue( "ETag.equals failed to compare to equal etag", etag1.equals( etag1 ) );
        assertTrue( "ETag.equals failed to compare to equal etag", etag2.equals( etag2 ) );
        assertTrue( "ETag.equals failed to compare to equal etag", etag3.equals( etag3 ) );
        assertTrue( "ETag.equals failed to compare to equal etag", etag4.equals( etag4 ) );
        assertTrue( "ETag.equals failed to compare to equal etag", etag5.equals( etag5 ) );

        assertTrue( "ETag.equals failed to compare to equal etag", etag1.equals( new ETag( etagValue1 ) ) );
        assertTrue( "ETag.equals failed to compare to equal etag", etag2.equals( new ETag( etagValue2 ) ) );
        assertTrue( "ETag.equals failed to compare to equal etag", etag3.equals( new ETag( etagValue3 ) ) );
        assertTrue( "ETag.equals failed to compare to equal etag", etag4.equals( new ETag( etagValue4 ) ) );
        assertTrue( "ETag.equals failed to compare to equal etag", etag5.equals( new ETag( etagValue5 ) ) );

        assertTrue( "ETag.equals failed to compare to equal etag", etag1.equals( etag2 ) );
        assertTrue( "ETag.equals failed to compare to equal etag", etag2.equals( etag1 ) );

        assertFalse( "ETag.equals failed to compare to unequal etag", etag1.equals( etag3 ) );
        assertFalse( "ETag.equals failed to compare to unequal etag", etag3.equals( etag1 ) );
        assertFalse( "ETag.equals failed to compare to unequal etag", etag1.equals( etag4 ) );
        assertFalse( "ETag.equals failed to compare to unequal etag", etag4.equals( etag1 ) );
        assertFalse( "ETag.equals failed to compare to unequal etag", etag5.equals( etag2 ) );
        
        assertFalse( "ETag.equals failed to compare to null", etag5.equals( null ) );

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

        assertEquals( "ETag.compareTo failed to compare to equal etag", etag1.compareTo( etag1 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag2.compareTo( etag2 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag3.compareTo( etag3 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag4.compareTo( etag4 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag5.compareTo( etag5 ), 0 );

        assertEquals( "ETag.compareTo failed to compare to equal etag", etag1.compareTo( etag5 ), -1 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag2.compareTo( etag1 ), 1 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag3.compareTo( etag2 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag4.compareTo( etag3 ), 1 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag5.compareTo( etag4 ), 1 );

        assertEquals( "ETag.compareTo failed to compare to equal etag", etag1.compareTo( etag2 ), -1 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag2.compareTo( etag3 ), 0 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag3.compareTo( etag4 ), -1 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag4.compareTo( etag5 ), -1 );
        assertEquals( "ETag.compareTo failed to compare to equal etag", etag5.compareTo( etag1 ), 1 );
        
        assertEquals( "ETag.compareTo failed to compare to null", etag5.compareTo( null ), 1 );


    }


    @Test
    public void testHashCode() throws InvalidETagException
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

        assertEquals( "ETag.hashCode failed to compare to equal etag", etag1.hashCode(  ), new ETag( etagValue1 ).hashCode() );
        assertEquals( "ETag.hashCode failed to compare to equal etag", etag2.hashCode(  ), new ETag( etagValue2 ).hashCode() );
        assertEquals( "ETag.hashCode failed to compare to equal etag", etag3.hashCode(  ), new ETag( etagValue3 ).hashCode() );
        assertEquals( "ETag.hashCode failed to compare to equal etag", etag4.hashCode(  ), new ETag( etagValue4 ).hashCode() );
        assertEquals( "ETag.hashCode failed to compare to equal etag", etag5.hashCode(  ), new ETag( etagValue5 ).hashCode() );

        assertNotEquals( "ETag.hashCode failed to compare to unequal etag", etag1.hashCode(  ), new ETag( etagValue5 ).hashCode() );
        assertNotEquals( "ETag.hashCode failed to compare to unequal etag", etag2.hashCode(  ), new ETag( etagValue1 ).hashCode() );
        assertEquals( "ETag.hashCode failed to compare to equal etag", etag3.hashCode(  ), new ETag( etagValue2 ).hashCode() );
        assertNotEquals( "ETag.hashCode failed to compare to unequal etag", etag4.hashCode(  ), new ETag( etagValue3 ).hashCode() );
        assertNotEquals( "ETag.hashCode failed to compare to unequal etag", etag5.hashCode(  ), new ETag( etagValue4 ).hashCode() );

        assertNotEquals( "ETag.hashCode failed to compare to unequal etag", etag1.hashCode(  ), new ETag( etagValue2 ).hashCode() );
        assertEquals( "ETag.hashCode failed to compare to equal etag", etag2.hashCode(  ), new ETag( etagValue3 ).hashCode() );
        assertNotEquals( "ETag.hashCode failed to compare to unequal etag", etag3.hashCode(  ), new ETag( etagValue4 ).hashCode() );
        assertNotEquals( "ETag.hashCode failed to compare to unequal etag", etag4.hashCode(  ), new ETag( etagValue5 ).hashCode() );
        assertNotEquals( "ETag.hashCode failed to compare to unequal etag", etag5.hashCode(  ), new ETag( etagValue1 ).hashCode() );

    }
    
    @Test
    public void testToHexString() throws InvalidETagException
    {
        byte[] etagValue1= { (byte) 0x00 };
        byte[] etagValue2={ (byte) 0xAF, (byte) 0xB9, (byte) 0x90 };
        byte[] etagValue3= { (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88 };
        String hexValue1= "00";
        String hexValue2= "afb990";
        String hexValue3= "1122334455667788";


        assertEquals( "ETag.toHexString gives wrong value" , ETag.toHexString(etagValue1), ( hexValue1 ) );
        assertEquals( "ETag.toHexString gives wrong value", ETag.toHexString(etagValue2), ( hexValue2 ) );
        assertEquals( "ETag.toHexString gives wrong value", ETag.toHexString(etagValue3), ( hexValue3 ) );
    }
    
    
    @Test
    public void testIsIn() throws InvalidETagException
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

        LinkedList<ETag> list= new LinkedList<ETag>();
        LinkedList<ETag> listofone= new LinkedList<ETag>();
        LinkedList<ETag> emptylist= new LinkedList<ETag>();
        
        list.add( etag1 );
        list.add( etag2 );
        list.add( etag3 );
        list.add( etag4 );
        
        listofone.add( etag4 );

        assertTrue( "ETag.isIn gives wrong value" , etag1.isIn( list ));
        assertTrue( "ETag.isIn gives wrong value" , etag2.isIn( list ));
        assertTrue( "ETag.isIn gives wrong value" , etag3.isIn( list ));
        assertTrue( "ETag.isIn gives wrong value" , etag4.isIn( list ));
        assertFalse( "ETag.isIn gives wrong value" , etag5.isIn( list ));
        
        assertTrue( "ETag.isIn gives wrong value" , new ETag( etagValue1 ).isIn( list ));
        assertTrue( "ETag.isIn gives wrong value" , new ETag( etagValue2 ).isIn( list ));
        assertTrue( "ETag.isIn gives wrong value" , new ETag( etagValue3 ).isIn( list ));
        assertTrue( "ETag.isIn gives wrong value" , new ETag( etagValue4 ).isIn( list ));
        assertFalse( "ETag.isIn gives wrong value", new ETag( etagValue5 ).isIn( list ));

        assertTrue( "ETag.isIn gives wrong value" , new ETag( etagValue4 ).isIn( listofone ));
        assertFalse( "ETag.isIn gives wrong value" , new ETag( etagValue5 ).isIn( listofone ));
        
        assertFalse( "ETag.isIn gives wrong value" , new ETag( etagValue4 ).isIn( emptylist ));
        assertFalse( "ETag.isIn gives wrong value" , new ETag( etagValue5 ).isIn( emptylist ));
    }

    
    @Test
    public void testGetList() throws InvalidETagException
    {
        byte[]  etagValue1= { (byte) 0x00,  };
        byte[]  etagValue2= { (byte) 0xAF, (byte) 0xB9, (byte) 0x90 };
        byte[]  etagValue3= { (byte) 0xAF, (byte) 0xB9, (byte) 0x90 };
        byte[]  etagValue4= { (byte) 0xAF, (byte) 0xB9, (byte) 0x91 };
        byte[]  etagValue5= { (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88 };

        ETag etag1= new ETag( etagValue1 );
        ETag etag2= new ETag( etagValue2 );
        ETag etag3= new ETag( etagValue3 );
        ETag etag4= new ETag( etagValue4 );
        ETag etag5= new ETag( etagValue5 );

        LinkedList< byte[] > byteslist= new LinkedList<byte[]>();
        LinkedList< byte[] > byteslistofone= new LinkedList<byte[]>();
        LinkedList< byte[] > bytesemptylist= new LinkedList<byte[]>();
        
        byteslist.add( etagValue1 );
        byteslist.add( etagValue2 );
        byteslist.add( etagValue3 );
        byteslist.add( etagValue4 );
        
        byteslistofone.add( etagValue4 );
        
        List<ETag> list= ETag.getList( byteslist );
        List<ETag> listofone= ETag.getList( byteslistofone );
        List<ETag> emptylist= ETag.getList( bytesemptylist );

        assertTrue( "ETag.getList doesn't contain etag" , etag1.isIn( list ));
        assertTrue( "ETag.getList doesn't contain etag" , etag2.isIn( list ));
        assertTrue( "ETag.getList doesn't contain etag" , etag3.isIn( list ));
        assertTrue( "ETag.getList doesn't contain etag" , etag4.isIn( list ));
        assertFalse( "ETag.getList does contain etag" , etag5.isIn( list ));
        
        assertTrue( "ETag.getList doesn't contain etag" , list.contains( etag1 ));
        assertTrue( "ETag.getList doesn't contain etag" , list.contains( etag2 ));
        assertTrue( "ETag.getList doesn't contain etag" , list.contains( etag3 ));
        assertTrue( "ETag.getList doesn't contain etag" , list.contains( etag4 ));
        assertFalse( "ETag.getList does contain etag" , list.contains( etag5 ));

        assertTrue( "ETag.getList doesn't contain etag" , new ETag( etagValue1 ).isIn( list ));
        assertTrue( "ETag.getList doesn't contain etag" , new ETag( etagValue2 ).isIn( list ));
        assertTrue( "ETag.getList doesn't contain etag" , new ETag( etagValue3 ).isIn( list ));
        assertTrue( "ETag.getList doesn't contain etag" , new ETag( etagValue4 ).isIn( list ));
        assertFalse( "ETag.getList does contain etag", new ETag( etagValue5 ).isIn( list ));

        assertTrue( "ETag.getList doesn't contain etag" , new ETag( etagValue4 ).isIn( listofone ));
        assertFalse( "ETag.getList does contain etag" , new ETag( etagValue5 ).isIn( listofone ));
        
        assertFalse( "ETag.getList does contain etag" , new ETag( etagValue4 ).isIn( emptylist ));
        assertFalse( "ETag.getList does contain etag" , new ETag( etagValue5 ).isIn( emptylist ));
    }


}
