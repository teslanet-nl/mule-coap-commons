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

package nl.teslanet.mule.transport.coap.commons.options;


import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.eclipse.californium.core.coap.BlockOption;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.OptionSet;

/**
 * {@code Options} is a collection of all options of a CoAP request or a response.
 * {@code Options} provides methods for converting Californium OptionSet to 
 * Mule message properties and vice versa.
 * When constructed based on a existing OptionSet it keeps a reference to this 
 * set in stead of making a deep copy, for performance reasons.
 * Notice that Californium OptionSet is not entirely thread-safe: hasObserve =&gt; (int) getObserve()
 * @see Option
 */
public class Options
{
    private OptionSet optionSet= null;

    /**
     * Default constructor
     */
    public Options()
    {
        super();
        setOptionSet( new OptionSet() );
    }

    /**
     * Constructs Options based on existing OptionSet.
     * Note that is keeps a reference to the OptionSet in stead of a deep copy - handle with care.
     * @param optionSet Set of options that will be used as a reference.
     */
    public Options( OptionSet optionSet )
    {
        super();
        this.optionSet= optionSet;
    }

    /**
     * Constructs Options based on a property map.
     * @param props The map of properties. 
     */
    public Options( Map< String, Object > props )
    {
        super();
        this.optionSet= new OptionSet();
        fillOptionSet( this.optionSet, props, false );

    }

    /**
     * Get the OptionSet.
     * @return the optionSet
     */
    public OptionSet getOptionSet()
    {
        return this.optionSet;
    }

    /**
     * Set the OptionSet reference.
     * @param optionSet the optionSet to use
     */
    public void setOptionSet( OptionSet optionSet )
    {
        this.optionSet= optionSet;
    }

//    private void assureBlock1Exists()
//    {
//        if ( !hasBlock1() )
//        {
//            setBlock1( new BlockOption() );
//        }
//
//    }
//
//    private void assureBlock2Exists()
//    {
//        if ( !hasBlock1() )
//        {
//            setBlock2( new BlockOption() );
//        }
//
//    }

    private static Long toLong( Object object )
    {
        if ( Long.class.isInstance( object ) )
        {
            return (Long) object;
        }
        else if ( Object.class.isInstance( object ) )
        {
            return Long.parseLong( object.toString() );
        }
        return null;
    }

    private static Integer toInteger( Object object )
    {
        if ( Integer.class.isInstance( object ) )
        {
            return (Integer) object;
        }
        else if ( Object.class.isInstance( object ) )
        {
            return Integer.parseInt( object.toString() );
        }
        return null;
    }

    private static Boolean toBoolean( Object object )
    {
        if ( Boolean.class.isInstance( object ) )
        {
            return (Boolean) object;
        }
        else if ( Object.class.isInstance( object ) )
        {
            return Boolean.parseBoolean( object.toString() );
        }
        return null;
    }

    private static byte[] toBytes( Object object )
    {
        if ( Object.class.isInstance( object ) )
        {
            if ( Byte[].class.isInstance( object ) )
            {
                return (byte[]) object;
            }
            else if ( byte[].class.isInstance( object ) )
            {
                return (byte[]) object;
            }
            else if ( ETag.class.isInstance( object ) )
            {
                return ( (ETag) object ).asBytes();
            }
            else
            {
                return object.toString().getBytes( CoAP.UTF8_CHARSET );
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static void fillOptionSet( OptionSet optionSet, Map< String, Object > props, boolean clear)
    {
        //make sure Optionset is empty, if needed
        if ( clear ) optionSet.clear();
        
        for ( Entry< String, Object > e : props.entrySet() )
        {
            /*OptionSet() */
            switch ( e.getKey() )
            {
                /* if_match_list       = null; // new LinkedList<byte[]>();*/
                case PropertyNames.COAP_OPT_IFMATCH_LIST:
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            optionSet.addIfMatch( toBytes( val ) );
                        }
                    }
                    else if ( Object.class.isInstance( e.getValue() ) )
                    {
                        optionSet.addIfMatch( toBytes( e.getValue() ) );
                    }
                    break;
                /*uri_host            = null; // from sender */
                case PropertyNames.COAP_OPT_URIHOST:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        //TODO support for comma separated values?
                        optionSet.setUriHost( e.getValue().toString() );
                    }
                    break;
                /*
                etag_list           = null; // new LinkedList<byte[]>();*/
                case PropertyNames.COAP_OPT_ETAG_LIST:
                    //TODO multiple values not valid in responses, add check here?
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            optionSet.addETag( toBytes( val ) );
                        }
                    }
                    else if ( Object.class.isInstance( e.getValue() ) )
                    {
                        //TODO support for comma separated values?
                        optionSet.addETag( toBytes( e.getValue() ) );
                    }
                    break;
                /*if_none_match       = false; */
                case PropertyNames.COAP_OPT_IFNONMATCH:
                    optionSet.setIfNoneMatch( toBoolean( e.getValue() ) );
                    break;

                /*
                uri_port            = null; // from sender*/
                case PropertyNames.COAP_OPT_URIPORT:
                    optionSet.setUriPort( toInteger( e.getValue() ) );
                    break;
                /*
                location_path_list  = null; // new LinkedList<String>();*/
                case PropertyNames.COAP_OPT_LOCATIONPATH_LIST:
                    //TODO check for duplication with LOCATIONPATH
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            optionSet.addLocationPath( val.toString() );
                        }
                    }
                    break;
                case PropertyNames.COAP_OPT_LOCATIONPATH:
                    //TODO prefix with "/" ?
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        optionSet.setLocationPath( e.getValue().toString() );
                    }
                    break;
                /*
                uri_path_list       = null; // new LinkedList<String>();*/
                case PropertyNames.COAP_OPT_URIPATH_LIST:
                    //TODO check for duplication with COAP_OPT_URIPATH
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            optionSet.addUriPath( val.toString() );
                        }
                    }
                    break;
                case PropertyNames.COAP_OPT_URIPATH:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        optionSet.setUriPath( e.getValue().toString() );
                    }
                    break;
                /*
                content_format      = null;*/
                case PropertyNames.COAP_OPT_CONTENTFORMAT:
                    //TODO add support for Content-Format?
                    //TODO add support for mime-type?
                    //TODO support for format as mime-type string
                    //TODO org.eclipse.californium.core.coap.MediaTypeRegistry
                    optionSet.setContentFormat( toInteger( e.getValue() ) );
                    break;
                /*
                max_age             = null;*/
                case PropertyNames.COAP_OPT_MAXAGE:

                    optionSet.setMaxAge( toLong( e.getValue() ) );
                    break;
                /*
                uri_query_list      = null; // new LinkedList<String>();*/
                case PropertyNames.COAP_OPT_URIQUERY_LIST:
                    //TODO check for duplication with COAP_OPT_URIQUERY
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            optionSet.addUriQuery( val.toString() );
                        }
                    }
                    break;
                case PropertyNames.COAP_OPT_URIQUERY:

                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        optionSet.setUriQuery( e.getValue().toString() );
                    }
                    break;
                /*
                accept              = null;*/
                case PropertyNames.COAP_OPT_ACCEPT:
                    //TODO add support for Content-Format?
                    //TODO add support for mime-type?
                    //TODO support for format as string
                    optionSet.setAccept( toInteger( e.getValue() ) );
                    break;
                /*
                location_query_list = null; // new LinkedList<String>();*/
                case PropertyNames.COAP_OPT_LOCATIONQUERY_LIST:
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            optionSet.addLocationQuery( val.toString() );
                        }
                    }
                    break;
                case PropertyNames.COAP_OPT_LOCATIONQUERY:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        optionSet.setLocationQuery( e.getValue().toString() );
                    }
                    break;
                /*
                proxy_uri           = null;*/
                case PropertyNames.COAP_OPT_PROXYURI:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        optionSet.setProxyUri( e.getValue().toString() );
                    }
                    break;
                /*
                proxy_scheme        = null;*/
                case PropertyNames.COAP_OPT_PROXYSCHEME:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        optionSet.setProxyScheme( e.getValue().toString() );
                    }
                    break;
                /*
                block1              = null;*/
                case PropertyNames.COAP_OPT_BLOCK1_SIZE:
                    //TODO check for duplicate with szx
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        int szx= BlockOption.size2Szx( toInteger( e.getValue() ) );
                        boolean m= false;
                        int num= 0; 
                        if ( optionSet.hasBlock1())
                        {
                            m= optionSet.getBlock1().isM();
                            num= optionSet.getBlock1().getNum();
                        }
                        optionSet.setBlock1(szx, m, num);
                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK1_SZX:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        int szx= toInteger( e.getValue() );
                        boolean m= false;
                        int num= 0; 
                        if ( optionSet.hasBlock1())
                        {
                            m= optionSet.getBlock1().isM();
                            num= optionSet.getBlock1().getNum();
                        }
                        optionSet.setBlock1(szx, m, num);
                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK1_NUM:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        int szx= 0;
                        boolean m= false;
                        int num= toInteger( e.getValue() ); 
                        if ( optionSet.hasBlock1())
                        {
                            szx= optionSet.getBlock1().getSzx();
                            m= optionSet.getBlock1().isM();
                        }
                        optionSet.setBlock1(szx, m, num);
                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK1_M:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        int szx= 0;
                        boolean m= toBoolean( e.getValue());
                        int num= 0; 
                        if ( optionSet.hasBlock1())
                        {
                            szx= optionSet.getBlock1().getSzx();
                            num= optionSet.getBlock1().getNum();
                        }
                        optionSet.setBlock1(szx, m, num);
                    }
                    break;
                /*
                block2              = null;*/
                case PropertyNames.COAP_OPT_BLOCK2_SIZE:
                    //TODO check for duplicate with szx
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        int szx= BlockOption.size2Szx( toInteger( e.getValue() ) );
                        boolean m= false;
                        int num= 0; 
                        if ( optionSet.hasBlock2())
                        {
                            m= optionSet.getBlock2().isM();
                            num= optionSet.getBlock2().getNum();
                        }
                        optionSet.setBlock2(szx, m, num);                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK2_SZX:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        int szx= toInteger( e.getValue() );
                        boolean m= false;
                        int num= 0; 
                        if ( optionSet.hasBlock2())
                        {
                            m= optionSet.getBlock2().isM();
                            num= optionSet.getBlock2().getNum();
                        }
                        optionSet.setBlock2(szx, m, num);
                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK2_NUM:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        int szx= 0;
                        boolean m= false;
                        int num= toInteger( e.getValue() ); 
                        if ( optionSet.hasBlock2())
                        {
                            szx= optionSet.getBlock2().getSzx();
                            m= optionSet.getBlock2().isM();
                        }
                        optionSet.setBlock2(szx, m, num);
                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK2_M:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        int szx= 0;
                        boolean m= toBoolean( e.getValue());
                        int num= 0; 
                        if ( optionSet.hasBlock2())
                        {
                            szx= optionSet.getBlock2().getSzx();
                            num= optionSet.getBlock2().getNum();
                        }
                        optionSet.setBlock2(szx, m, num);
                    }
                    break;
                /*
                 * size1               = null;*/
                case PropertyNames.COAP_OPT_SIZE1:
                    optionSet.setSize1( toInteger( e.getValue() ) );
                    break;
                /*
                size2               = null;*/
                case PropertyNames.COAP_OPT_SIZE2:
                    optionSet.setSize2( toInteger( e.getValue() ) );
                    break;
                /*
                observe             = null;*/
                case PropertyNames.COAP_OPT_OBSERVE:
                    optionSet.setObserve( toInteger( e.getValue() ) );
                    break;

                default:
                    /*               
                    others              = null; // new LinkedList<>();
                    */
                    Integer optionNr= optionNrfromPropertyName( e.getKey() );
                    if ( optionNr >= 0 && e.getValue() != null )
                    {
                        Object o= e.getValue();
                        Option option= new Option( optionNr );
                        if ( byte[].class.isAssignableFrom( o.getClass() ) )
                        {
                            option.setValue( (byte[]) o );
                            optionSet.addOption( option );
                        }
                        else
                        {
                            option.setStringValue( o.toString() );
                            optionSet.addOption( option );
                        }
                    }
            }
        }
    }

    public static void fillPropertyMap( OptionSet options, Map< String, Object > props ) throws InvalidOptionValueException 
    {
        String msg= "cannot create property";
        // List<byte[]> if_match_list;
        if ( !options.getIfMatch().isEmpty() )
        {
            String propertyName= PropertyNames.COAP_OPT_IFMATCH_LIST;           
            try
            {
                props.put( propertyName, ETag.getList( options.getIfMatch() ) );
            }
            catch ( InvalidETagException e )
            {
                throw new InvalidOptionValueException( propertyName, msg, e);
            }
        }
        // String       uri_host;
        if ( options.hasUriHost() )
        {
            props.put( PropertyNames.COAP_OPT_URIHOST, options.getUriHost() );
        }
        // List<byte[]> etag_list;
        if ( !options.getETags().isEmpty() )
        {
            String propertyName= PropertyNames.COAP_OPT_ETAG_LIST;           
            try
            {
                props.put( propertyName, ETag.getList( options.getETags() ) );
            }
            catch ( InvalidETagException e )
            {
                throw new InvalidOptionValueException( propertyName, msg, e);
            }
        }
        // boolean      if_none_match; // true if option is set
        props.put( PropertyNames.COAP_OPT_IFNONMATCH, Boolean.valueOf( options.hasIfNoneMatch() ) );

        // Integer      uri_port; // null if no port is explicitly defined
        if ( options.hasUriPort() )
        {
            props.put( PropertyNames.COAP_OPT_URIPORT, options.getUriPort() );
        }
        // List<String> location_path_list;
        if ( !options.getLocationPath().isEmpty() )
        {
            props.put( PropertyNames.COAP_OPT_LOCATIONPATH_LIST, options.getLocationPath() );
            props.put( PropertyNames.COAP_OPT_LOCATIONPATH, options.getLocationPathString() );
        }
        // List<String> uri_path_list;
        if ( !options.getUriPath().isEmpty() )
        {
            props.put( PropertyNames.COAP_OPT_URIPATH_LIST, options.getUriPath() );
            props.put( PropertyNames.COAP_OPT_URIPATH, options.getUriPathString() );
        }
        // Integer      content_format;
        if ( options.hasContentFormat() )
        {
            //TODO as text?
            props.put( PropertyNames.COAP_OPT_CONTENTFORMAT, Integer.valueOf( options.getContentFormat() ) );
        }
        // Long         max_age; // (0-4 bytes)
        if ( options.hasMaxAge() )
        {
            props.put( PropertyNames.COAP_OPT_MAXAGE, options.getMaxAge() );
        }
        // List<String> uri_query_list;
        if ( !options.getUriQuery().isEmpty() )
        {
            props.put( PropertyNames.COAP_OPT_URIQUERY_LIST, options.getUriQuery() );
            props.put( PropertyNames.COAP_OPT_URIQUERY, options.getUriQueryString() );
        }
        // Integer      accept;
        if ( options.hasAccept() )
        {
            props.put( PropertyNames.COAP_OPT_ACCEPT, Integer.valueOf( options.getAccept() ) );
        }
        // List<String> location_query_list;
        if ( !options.getLocationQuery().isEmpty() )
        {
            props.put( PropertyNames.COAP_OPT_LOCATIONQUERY_LIST, options.getLocationQuery() );
            props.put( PropertyNames.COAP_OPT_LOCATIONQUERY, options.getLocationQueryString() );
        }
        // String       proxy_uri;
        if ( options.hasProxyUri() )
        {
            props.put( PropertyNames.COAP_OPT_PROXYURI, options.getProxyUri() );
        }
        // String       proxy_scheme;
        if ( options.hasProxyScheme() )
        {
            props.put( PropertyNames.COAP_OPT_PROXYSCHEME, options.getProxyScheme() );
        }
        // BlockOption  block1;
        if ( options.hasBlock1() )
        {
            props.put( PropertyNames.COAP_OPT_BLOCK1_SZX, Integer.valueOf( options.getBlock1().getSzx() ) );
            props.put( PropertyNames.COAP_OPT_BLOCK1_SIZE, Integer.valueOf( options.getBlock1().getSize() ) );
            props.put( PropertyNames.COAP_OPT_BLOCK1_NUM, Integer.valueOf( options.getBlock1().getNum() ) );
            props.put( PropertyNames.COAP_OPT_BLOCK1_M, Boolean.valueOf( options.getBlock1().isM() ) );
        }
        // BlockOption  block2;
        if ( options.hasBlock2() )
        {
            props.put( PropertyNames.COAP_OPT_BLOCK2_SZX, Integer.valueOf( options.getBlock2().getSzx() ) );
            props.put( PropertyNames.COAP_OPT_BLOCK2_SIZE, Integer.valueOf( options.getBlock2().getSize() ) );
            props.put( PropertyNames.COAP_OPT_BLOCK2_NUM, Integer.valueOf( options.getBlock2().getNum() ) );
            props.put( PropertyNames.COAP_OPT_BLOCK2_M, Boolean.valueOf( options.getBlock2().isM() ) );
        }
        // Integer      size1;
        if ( options.hasSize1() )
        {
            props.put( PropertyNames.COAP_OPT_SIZE1, options.getSize1() );
        }
        // Integer      size2;
        if ( options.hasSize2() )
        {
            props.put( PropertyNames.COAP_OPT_SIZE2, options.getSize2() );
        }
        // Integer      observe;
        if ( options.hasObserve() )
        {
            props.put( PropertyNames.COAP_OPT_OBSERVE, options.getObserve() );
        }
        // Arbitrary options
        // List<Option> others;
        for ( Option other : options.getOthers() )
        {
            props.put( PropertyNames.PREFIX_COAP_OPT_OTHER + other.getNumber(), other.getValue() );
            props.put( PropertyNames.PREFIX_COAP_OPT_OTHER + other.getNumber() + PropertyNames.POSTFIX_CRITICAL, Boolean.valueOf( other.isCritical() ) );
            props.put( PropertyNames.PREFIX_COAP_OPT_OTHER + other.getNumber() + PropertyNames.POSTFIX_NOCACHEKEY, Boolean.valueOf( other.isNoCacheKey() ) );
            props.put( PropertyNames.PREFIX_COAP_OPT_OTHER + other.getNumber() + PropertyNames.POSTFIX_UNSAFE, Boolean.valueOf( other.isUnSafe() ) );
        }
    }

    private static int optionNrfromPropertyName( String propertyName )
    {
        Matcher matcher= PropertyNames.otherPattern.matcher( propertyName );

        // if an occurrence if a pattern was found in a given string...
        if ( matcher.find() )
        {
            return Integer.parseInt( matcher.group( 1 ) );
        }
        else
        {
            return -1;
        }
    }

}