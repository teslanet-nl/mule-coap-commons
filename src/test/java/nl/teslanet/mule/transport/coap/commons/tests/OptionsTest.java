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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.californium.core.coap.OptionSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.teslanet.mule.transport.coap.commons.options.ETag;
import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;
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
    public void testConstructorOneProperty()
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        props.put( "coap.opt.observe", 123 );

        Options options= null;
        options= new Options( props );

        assertNotNull( "Options contruction with empty property map failed", options );
        assertEquals( "Options contruction with one property failed", options.getOptionSet().getObserve(), new Integer( 123 ) );
    }

    @Test
    public void testSetGetOptionSet()
    {
        OptionSet optionSet= new OptionSet();

        Options options= new Options();
        options.setOptionSet( optionSet );

        assertNotNull( "Options contruction with optionset failed", options );
        assertTrue( "Options getter and setter failed to deliver the same optionSet", options.getOptionSet() == optionSet );
    }

    @Test
    public void testSetOptionSet()
    {
        OptionSet optionSet= new OptionSet();

        Options options= new Options();
        options.setOptionSet( optionSet );

        assertNotNull( "Options contruction with optionset failed", options );
        assertTrue( "Options getter and setter failed to deliver the same optionSet", options.getOptionSet() == optionSet );
    }

    @Test
    public void testLongProperty()
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        Long value= new Long( 45 );
        props.put( "coap.opt.observe", value );

        Options options= new Options( props );
        options.getOptionSet().getObserve();

        assertEquals( "Options contruction with empty property map failed", options.getOptionSet().getObserve(), new Integer( 45 ) );
    }

    @Test
    public void testIntegerProperty()
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        Integer value= new Integer( 45 );
        props.put( "coap.opt.observe", value );

        Options options= new Options( props );
        options.getOptionSet().getObserve();

        assertEquals( "Options contruction with empty property map failed", options.getOptionSet().getObserve(), new Integer( 45 ) );
    }

    @Test
    public void testStringProperty()
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        String value= new String( "45" );
        props.put( "coap.opt.observe", value );

        Options options= new Options( props );
        options.getOptionSet().getObserve();

        assertEquals( "Options contruction with empty property map failed", options.getOptionSet().getObserve(), new Integer( 45 ) );
    }

    @Test
    public void testOptionSetIfMatch() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        byte[] etagValue1= { (byte) 0x00, (byte) 0xFF };
        byte[] etagValue2= { (byte) 0x11, (byte) 0xFF };
        set.addIfMatch( etagValue1 );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        @SuppressWarnings("unchecked")
        List< ETag > list= (List< ETag >) props.get( "coap.opt.if_match.list" );

        assertNotNull( list );
        assertEquals( "coap.opt.if_match.list: wrong number of etags", 1, list.size() );
        assertTrue( "coap.opt.if_match.list: missing etag", list.contains( new ETag( etagValue1 ) ) );
        assertFalse( "coap.opt.if_match.list: etag not expected", list.contains( new ETag( etagValue2 ) ) );
    }

    @Test
    public void testMapIfMatch() throws InvalidETagException
    {
        byte[] etagValue1= { (byte) 0x00, (byte) 0xFF };
        byte[] etagValue2= { (byte) 0x11, (byte) 0xFF };

        HashMap< String, Object > props= new HashMap< String, Object >();
        props.put( "coap.opt.if_match.list", new ETag( etagValue1 ) );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "if_match option has wrong count", 1, set.getIfMatchCount() );

        List< ETag > list= ETag.getList( set.getIfMatch() );
        assertEquals( "coap.opt.if_match.list: wrong number of etags", 1, list.size() );
        assertTrue( "coap.opt.if_match.list: missing etag", list.contains( new ETag( etagValue1 ) ) );
        assertFalse( "coap.opt.if_match.list: etag not expected", list.contains( new ETag( etagValue2 ) ) );

        props.clear();
        props.put( "coap.opt.if_match.list", etagValue1 );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "if_match option has wrong count", 1, set.getIfMatchCount() );

        list= ETag.getList( set.getIfMatch() );
        assertEquals( "coap.opt.if_match.list: wrong number of etags", 1, list.size() );
        assertTrue( "coap.opt.if_match.list: missing etag", list.contains( new ETag( etagValue1 ) ) );
        assertFalse( "coap.opt.if_match.list: etag not expected", list.contains( new ETag( etagValue2 ) ) );
    }

    @Test
    public void testOptionSetIfMatchMultiple() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        byte[] etagValue1= { (byte) 0x00, (byte) 0xFF };
        byte[] etagValue2= { (byte) 0x11, (byte) 0xFF };
        byte[] etagValue3= { (byte) 0x22, (byte) 0xFF };

        set.addIfMatch( new ETag( etagValue1 ).asBytes() );
        set.addIfMatch( new ETag( etagValue2 ).asBytes() );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        @SuppressWarnings("unchecked")
        List< ETag > list= (List< ETag >) props.get( "coap.opt.if_match.list" );

        assertNotNull( list );
        assertEquals( "coap.opt.if_match.list: wrong number of etags", 2, list.size() );
        assertTrue( "coap.opt.if_match.list: missing etag", list.contains( new ETag( etagValue1 ) ) );
        assertTrue( "coap.opt.if_match.list: missing etag", list.contains( new ETag( etagValue2 ) ) );
        assertFalse( "coap.opt.if_match.list: etag not expected", list.contains( new ETag( etagValue3 ) ) );
    }

    @Test
    public void testMapIfMatchMultiple() throws InvalidETagException
    {
        byte[] etagValue1= { (byte) 0x00, (byte) 0xFF };
        byte[] etagValue2= { (byte) 0x11, (byte) 0xFF };
        byte[] etagValue3= { (byte) 0x22, (byte) 0xFF };

        HashMap< String, Object > props= new HashMap< String, Object >();
        LinkedList< ETag > list= new LinkedList< ETag >();
        list.add( new ETag( etagValue1 ) );
        list.add( new ETag( etagValue2 ) );
        props.put( "coap.opt.if_match.list", list );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "if_match option has wrong count", 2, set.getIfMatchCount() );

        List< ETag > etagslist= ETag.getList( set.getIfMatch() );
        assertTrue( "if_match option missing", etagslist.contains( new ETag( etagValue1 ) ) );
        assertTrue( "if_match option missing", etagslist.contains( new ETag( etagValue2 ) ) );

        props.clear();
        LinkedList< byte[] > bytelist= new LinkedList< byte[] >();
        bytelist.add( etagValue1 );
        bytelist.add( etagValue2 );
        props.put( "coap.opt.if_match.list", list );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "if_match option has wrong count", 2, set.getIfMatchCount() );

        etagslist= ETag.getList( set.getIfMatch() );
        assertEquals( "coap.opt.if_match.list: wrong number of etags", 2, etagslist.size() );
        assertTrue( "coap.opt.if_match.list: missing etag", etagslist.contains( new ETag( etagValue1 ) ) );
        assertTrue( "coap.opt.if_match.list: missing etag", etagslist.contains( new ETag( etagValue2 ) ) );
        assertFalse( "coap.opt.if_match.list: etag not expected", etagslist.contains( new ETag( etagValue3 ) ) );
    }

    @Test
    public void testOptionUrihost() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        String host= "testhost";
        set.setUriHost( host );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        assertEquals( "coap.opt.if_match.list: wrong number of etags", props.get( "coap.opt.uri_host" ), host );
    }

    @Test
    public void testMapUrihost() throws InvalidETagException
    {
        String host= "testhost";

        HashMap< String, Object > props= new HashMap< String, Object >();
        props.put( "coap.opt.uri_host", host );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "uri_host option has wrong value", set.getUriHost(), host );

        props.clear();
        props.put( "coap.opt.uri_host", new StringWrapper( host ) );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "uri_host option has wrong value", set.getUriHost(), host );
    }

    @Test
    public void testOptionSetETag() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        byte[] etagValue1= { (byte) 0x00, (byte) 0xFF };
        byte[] etagValue2= { (byte) 0x11, (byte) 0xFF };
        set.addETag( etagValue1 );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        @SuppressWarnings("unchecked")
        List< ETag > list= (List< ETag >) props.get( "coap.opt.etag.list" );

        assertNotNull( list );
        assertEquals( "coap.opt.etag.list: wrong number of etags", 1, list.size() );
        assertTrue( "coap.opt.etag.list: missing etag", list.contains( new ETag( etagValue1 ) ) );
        assertFalse( "coap.opt.etag.list: etag not expected", list.contains( new ETag( etagValue2 ) ) );
    }

    @Test
    public void testMapETag() throws InvalidETagException
    {
        byte[] etagValue1= { (byte) 0x00, (byte) 0xFF };
        byte[] etagValue2= { (byte) 0x11, (byte) 0xFF };

        HashMap< String, Object > props= new HashMap< String, Object >();
        props.put( "coap.opt.etag.list", new ETag( etagValue1 ) );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "etag option has wrong count", set.getETagCount(), 1 );

        List< ETag > list= ETag.getList( set.getETags() );
        assertEquals( "coap.opt.etag.list: wrong number of etags", 1, list.size() );
        assertTrue( "coap.opt.etag.list: missing etag", list.contains( new ETag( etagValue1 ) ) );
        assertFalse( "coap.opt.etag.list: etag not expected", list.contains( new ETag( etagValue2 ) ) );

        props.clear();
        props.put( "coap.opt.etag.list", etagValue1 );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "etag option has wrong count", 1, set.getETagCount() );

        list= ETag.getList( set.getETags() );
        assertEquals( "coap.opt.etag.list: wrong number of etags", 1, list.size() );
        assertTrue( "coap.opt.etag.list: missing etag", list.contains( new ETag( etagValue1 ) ) );
        assertFalse( "coap.opt.etag.list: etag not expected", list.contains( new ETag( etagValue2 ) ) );
    }

    @Test
    public void testOptionSetETagMultiple() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        byte[] etagValue1= { (byte) 0x00, (byte) 0xFF };
        byte[] etagValue2= { (byte) 0x11, (byte) 0xFF };
        byte[] etagValue3= { (byte) 0x22, (byte) 0xFF };

        set.addETag( new ETag( etagValue1 ).asBytes() );
        set.addETag( new ETag( etagValue2 ).asBytes() );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        @SuppressWarnings("unchecked")
        List< ETag > list= (List< ETag >) props.get( "coap.opt.etag.list" );

        assertNotNull( list );
        assertEquals( "coap.opt.etag.list: wrong number of etags", 2, list.size() );
        assertTrue( "coap.opt.etag.list: missing etag", list.contains( new ETag( etagValue1 ) ) );
        assertTrue( "coap.opt.etag.list: missing etag", list.contains( new ETag( etagValue2 ) ) );
        assertFalse( "coap.opt.etag.list: etag not expected", list.contains( new ETag( etagValue3 ) ) );
    }

    @Test
    public void testMapETagMultiple() throws InvalidETagException
    {
        byte[] etagValue1= { (byte) 0x00, (byte) 0xFF };
        byte[] etagValue2= { (byte) 0x11, (byte) 0xFF };
        byte[] etagValue3= { (byte) 0x22, (byte) 0xFF };

        HashMap< String, Object > props= new HashMap< String, Object >();
        LinkedList< ETag > list= new LinkedList< ETag >();
        list.add( new ETag( etagValue1 ) );
        list.add( new ETag( etagValue2 ) );
        props.put( "coap.opt.etag.list", list );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "etag option has wrong count", set.getETagCount(), 2 );

        List< ETag > etagslist= ETag.getList( set.getETags() );
        assertTrue( "etag option missing", etagslist.contains( new ETag( etagValue1 ) ) );
        assertTrue( "etag option missing", etagslist.contains( new ETag( etagValue2 ) ) );

        props.clear();
        LinkedList< byte[] > bytelist= new LinkedList< byte[] >();
        bytelist.add( etagValue1 );
        bytelist.add( etagValue2 );
        props.put( "coap.opt.etag.list", list );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "if_match option has wrong count", set.getETagCount(), 2 );

        etagslist= ETag.getList( set.getETags() );
        assertEquals( "coap.opt.etag.list: wrong number of etags", 2, etagslist.size() );
        assertTrue( "coap.opt.etag.list: missing etag", etagslist.contains( new ETag( etagValue1 ) ) );
        assertTrue( "coap.opt.etag.list: missing etag", etagslist.contains( new ETag( etagValue2 ) ) );
        assertFalse( "coap.opt.etag.list: etag not expected", etagslist.contains( new ETag( etagValue3 ) ) );
    }

    @Test
    public void testOptionIfNoneMatch() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        set.setIfNoneMatch( new Boolean( true ) );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        assertTrue( "coap.opt.if_none_match: wrong value", (Boolean) props.get( "coap.opt.if_none_match" ) );

        set.setIfNoneMatch( new Boolean( false ) );

        props.clear();

        Options.fillProperties( set, props );

        assertFalse( "coap.opt.if_none_match: wrong value", (Boolean) props.get( "coap.opt.if_none_match" ) );
    }

    @Test
    public void testMapIfNoneMatch() throws InvalidETagException
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        props.put( "coap.opt.if_none_match", new Boolean( true ) );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertTrue( "coap.opt.if_none_match: wrong value", set.hasIfNoneMatch() );

        props.clear();
        props.put( "coap.opt.if_none_match", new StringWrapper( "true" ) );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertTrue( "coap.opt.if_none_match: wrong value", set.hasIfNoneMatch() );

        props.clear();
        props.put( "coap.opt.if_none_match", new Boolean( false ) );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertFalse( "coap.opt.if_none_match: wrong value", set.hasIfNoneMatch() );

        props.clear();
        props.put( "coap.opt.if_none_match", new StringWrapper( "false" ) );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertFalse( "coap.opt.if_none_match: wrong value", set.hasIfNoneMatch() );
    }

    @Test
    public void testOptionUriPort() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        Integer port= 5536;
        set.setUriPort( port );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        assertEquals( "coap.opt.uri_port: wrong value", port, (Integer) props.get( "coap.opt.uri_port" ) );
    }

    @Test
    public void testMapUriPort() throws InvalidETagException
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        Integer port= 5336;
        props.put( "coap.opt.uri_port", port );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.uri_port: wrong value", port, set.getUriPort() );

        props.clear();
        props.put( "coap.opt.uri_port", new StringWrapper( "5337" ) );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.uri_port: wrong value", new Integer( 5337 ), set.getUriPort() );

    }

    @Test
    public void testOptionSetLocationPath() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        String value1= "this";
        String value2= "is";
        String value3= "some location";
        String total= "this/is";

        set.addLocationPath( value1 );
        set.addLocationPath( value2 );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        @SuppressWarnings("unchecked")
        List< ETag > list= (List< ETag >) props.get( "coap.opt.location_path.list" );

        assertNotNull( list );
        assertEquals( "coap.opt.location_path.list: wrong number of path segment", 2, list.size() );
        assertTrue( "coap.opt.location_path.list: missing path segment", list.contains( value1 ) );
        assertTrue( "coap.opt.location_path.list: missing path segment", list.contains( value2 ) );
        assertFalse( "coap.opt.location_path.list: path segment not expected", list.contains( value3 ) );
        assertEquals( "coap.opt.location_path: wrong path", total, props.get( "coap.opt.location_path" ) );

    }

    @Test
    public void testMapLocationPath() throws InvalidETagException
    {
        String value1= "this";
        String value2= "is";
        String value3= "some location";
        String total= "this/is";

        HashMap< String, Object > props= new HashMap< String, Object >();
        LinkedList< String > list= new LinkedList< String >();
        list.add( value1 );
        list.add( value2 );
        props.put( "coap.opt.location_path.list", list );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.location_path.list: wrong number of path segment", 2, set.getLocationPathCount() );
        List< String > locationlist= set.getLocationPath();
        assertEquals( "coap.opt.location_path.list: missing path segment", value1, locationlist.get( 0 ) );
        assertEquals( "coap.opt.location_path.list: missing path segment", value2, locationlist.get( 1 ) );
        assertFalse( "coap.opt.location_path.list: path segment not expected", locationlist.contains( value3 ) );
        assertEquals( "coap.opt.location_path: wrong path", total, set.getLocationPathString() );

        props.clear();
        props.put( "coap.opt.location_path", total );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.location_path.list: wrong number of path segment", 2, set.getLocationPathCount() );
        locationlist= set.getLocationPath();
        assertEquals( "coap.opt.location_path.list: missing path segment", value1, locationlist.get( 0 ) );
        assertEquals( "coap.opt.location_path.list: missing path segment", value2, locationlist.get( 1 ) );
        assertFalse( "coap.opt.location_path.list: path segment not expected", locationlist.contains( value3 ) );
        assertEquals( "coap.opt.location_path: wrong path", total, set.getLocationPathString() );
    }

    @Test
    public void testOptionSetUriPath() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        String value1= "this";
        String value2= "is";
        String value3= "some path";
        String total= "this/is";

        set.addUriPath( value1 );
        set.addUriPath( value2 );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        @SuppressWarnings("unchecked")
        List< ETag > list= (List< ETag >) props.get( "coap.opt.uri_path.list" );

        assertNotNull( list );
        assertEquals( "coap.opt.uri_path.list: wrong number of path segment", 2, list.size() );
        assertEquals( "coap.opt.location_path.list: missing path segment", value1, list.get( 0 ) );
        assertEquals( "coap.opt.location_path.list: missing path segment", value2, list.get( 1 ) );
        assertFalse( "coap.opt.location_path.list: path segment not expected", list.contains( value3 ) );
        assertEquals( "coap.opt.location_path: wrong path", total, props.get( "coap.opt.uri_path" ) );

    }

    @Test
    public void testMapUriPath() throws InvalidETagException
    {
        String value1= "this";
        String value2= "is";
        String value3= "some path";
        String total= "this/is";

        HashMap< String, Object > props= new HashMap< String, Object >();
        LinkedList< String > list= new LinkedList< String >();
        list.add( value1 );
        list.add( value2 );
        props.put( "coap.opt.uri_path.list", list );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.uri_path.list: wrong number of path segment", 2, set.getURIPathCount() );
        List< String > uripathlist= set.getUriPath();
        assertEquals( "coap.opt.uri_path.list: missing path segment", value1 , uripathlist.get( 0 ));
        assertEquals( "coap.opt.uri_path.list: missing path segment", value2, uripathlist.get( 1 ) );
        assertFalse( "coap.opt.uri_path.list: path segment not expected", uripathlist.contains( value3 ) );
        assertEquals( "coap.opt.uri_path: wrong path", total, set.getUriPathString() );

        props.clear();
        props.put( "coap.opt.uri_path", total );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.uri_path.list: wrong number of path segment", 2, set.getURIPathCount() );
        uripathlist= set.getUriPath();
        assertEquals( "coap.opt.uri_path.list: missing path segment", value1, uripathlist.get( 0 ) );
        assertEquals( "coap.opt.uri_path.list: missing path segment", value2, uripathlist.get( 1 ) );
        assertFalse( "coap.opt.uri_path.list: path segment not expected", uripathlist.contains( value3 ) );
        assertEquals( "coap.opt.uri_path: wrong path", total, set.getUriPathString() );
    }


    @Test
    public void testOptionContentFormat() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        Integer format= 41;
        set.setContentFormat( format );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        assertEquals( "coap.opt.content_format: wrong value", format, (Integer) props.get( "coap.opt.content_format" ) );
    }

    @Test
    public void testMapContentFormat() throws InvalidETagException
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        Integer format= 40;
        props.put( "coap.opt.content_format", format );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.content_format: wrong value", format, new Integer( set.getContentFormat() ));

        props.clear();
        props.put( "coap.opt.content_format", new String( "40" ) );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.content_format: wrong value", format, new Integer( set.getContentFormat() ));

        props.clear();
        props.put( "coap.opt.content_format", new StringWrapper( "40" ) );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.content_format: wrong value", format, new Integer( set.getContentFormat() ));

     }


    @Test
    public void testOptionMaxAge() throws InvalidETagException
    {
        OptionSet set= new OptionSet();
        Long maxage= new Long( 120 );
        set.setMaxAge( maxage );

        HashMap< String, Object > props= new HashMap< String, Object >();

        Options.fillProperties( set, props );

        assertEquals( "coap.opt.max_age: wrong value", maxage, (Long) props.get( "coap.opt.max_age" ) );
    }

    @Test
    public void testMapMaxAge() throws InvalidETagException
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        Long maxage= new Long( 120 );
        props.put( "coap.opt.max_age", maxage );

        OptionSet set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.max_age: wrong value", maxage, set.getMaxAge() );

        props.clear();
        props.put( "coap.opt.max_age", new Integer( 120 ) );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.max_age: wrong value", maxage, set.getMaxAge() );

        props.clear();
        props.put( "coap.opt.max_age", new String( "120" ) );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.max_age: wrong value", maxage, set.getMaxAge() );

        props.clear();
        props.put( "coap.opt.max_age", new StringWrapper( "120" ) );

        set= new OptionSet();
        Options.fillOptionSet( set, props, true );

        assertEquals( "coap.opt.max_age: wrong value", maxage, set.getMaxAge() );

     }
    
    private class StringWrapper
    {
        private String string;

        public StringWrapper( String string )
        {
            this.string= string;
        }

        @Override
        public String toString()
        {
            return string;
        }
    }
}
