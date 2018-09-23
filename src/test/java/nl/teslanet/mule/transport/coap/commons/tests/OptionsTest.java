/*******************************************************************************
 * Copyright (c) 2017, 2018 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Public License - v 2.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/

package nl.teslanet.mule.transport.coap.commons.tests;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.eclipse.californium.core.coap.OptionSet;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.teslanet.mule.transport.coap.commons.options.ETag;
import nl.teslanet.mule.transport.coap.commons.options.Options;


/**
 * @author Rogier Cobben
 *
 */
public class OptionsTest
{
    @Rule
    public ExpectedException exception= ExpectedException.none();

    @Test
    public void testConstructorDefault()
    {
        Options options= null;
        
        options= new Options();

        assertNotNull( "Options default contruction failed", options );
    }

    @Test
    public void testConstructorOptionSet()
    {
        OptionSet optionSet= new OptionSet();

        Options options= null;
        options= new Options( optionSet );

        assertNotNull( "Options contruction with optionset failed", options );
        assertTrue( "Options contuction failed to deliver the same optionSet", options.getOptionSet() == optionSet );
    }
    
    @Test
    public void testConstructorEmptyProperties()
    {
        HashMap< String, Object > props= new HashMap< String, Object >();

        Options options= null;      
        options= new Options( props );


        assertNotNull( "Options contruction with empty property map failed", options );
    }
    
    @Test
    public void testConstructorOptionSetGetSet()
    {
        OptionSet optionSet= new OptionSet();

        Options options= new Options(  );
        options.setOptionSet( optionSet );

        assertNotNull( "Options contruction with optionset failed", options );
        assertTrue( "Options getter and setter failed to deliver the same optionSet", options.getOptionSet() == optionSet );
    }    
    
    @Test
    public void testEquals()
    {
        String etagValue1= "ffb990";
        byte[] etagValue2= { (byte) 0xFF, (byte) 0xB9, (byte) 0x90 };
        String etagValue3= "afb991";
        String etagValue4= "afb99100112233";

        ETag etag1= new ETag( etagValue1 );
        ETag etag2= new ETag( etagValue2 );
        ETag etag3= new ETag( etagValue3 );
        ETag etag4= new ETag( etagValue4 );

        assertTrue( "ETag.equals failed to compare to equal etags", etag1.equals( etag1 ) );

        assertTrue( "ETag.equals failed to compare to equal etags", etag1.equals( etag2 ) );
        assertTrue( "ETag.equals failed to compare to equal etags", etag2.equals( etag1 ) );

        assertFalse( "ETag.equals failed to compare to unequal etags", etag1.equals( etag3 ) );
        assertFalse( "ETag.equals failed to compare to unequal etags", etag3.equals( etag1 ) );
        assertFalse( "ETag.equals failed to compare to unequal etags", etag1.equals( etag4 ) );
        assertFalse( "ETag.equals failed to compare to unequal etags", etag4.equals( etag1 ) );
    }

    @Ignore //validation is done by californium?
    @Test
    public void tooLongString() throws Exception
    {
        String etagValue1= "112233445566778899";
        ETag etag1= null;
        exception.expect( Exception.class );
        //exception.expectMessage(containsString("exception message"));
        etag1= new ETag( etagValue1 );
        org.junit.Assert.assertNotNull( etag1 );
    }
}
