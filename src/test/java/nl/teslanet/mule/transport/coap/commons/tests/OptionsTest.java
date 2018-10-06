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


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.eclipse.californium.core.coap.OptionSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
    public void testConstructorOptionsetGetset()
    {
        OptionSet optionSet= new OptionSet();

        Options options= new Options(  );
        options.setOptionSet( optionSet );

        assertNotNull( "Options contruction with optionset failed", options );
        assertTrue( "Options getter and setter failed to deliver the same optionSet", options.getOptionSet() == optionSet );
    }    

}
