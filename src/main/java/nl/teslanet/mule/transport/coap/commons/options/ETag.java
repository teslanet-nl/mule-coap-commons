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
import java.util.LinkedList;
import java.util.List;

//TODO size of etag should be limited to 1 to 8 bytes
//TODO constructor for long for convenience:
//Long millis= ...;
//ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
//buffer.putLong( millis );
//etags.put( key, new ETag( buffer.array()));

/**
 * Implementation of the Etag concept for convenience.
 * It eases the handling of Etag values in Mule flows. 
 * Etags can be constructed from byte array as well as from strings 
 * containing the hexadecimal representation. 
 * An etag is immutable.
 * @author Rogier Cobben
 *
 */
/**
 * @author rogier
 *
 */
public final class ETag implements Cloneable, Comparable< ETag >
{
    /**
     * The etag value
     */
    private final byte[] value;
   
    /**
     * Constructs an etag from a byte array value.
     * @param etag byte array containing the etag value
     * @throws InvalidETagException when given etag has not a length of 1..8 bytes
     */
    public ETag( byte[] etag ) throws InvalidETagException
    {
        if ( etag == null)
        {
            throw new InvalidETagException( "ETag value null is not allowed"); 
        }
        if ( etag.length < 1 || etag.length > 8 )
        {
            throw new InvalidETagException( "ETag length invalid, must be between 1..8 bytes. Given length is: " + etag.length );
        }      
        this.value= etag.clone();
        
    }

    /**
     * Constructs an etag from a string containing the hexadecimal representation, like '11FF'
     * @param etag Byte array containing the etag value.
     * @throws InvalidETagException when given string does not represent a etag length of 1..8 bytes
     */
    public ETag( String hexString ) throws InvalidETagException
    {
        if ( hexString == null )
        {
            throw new InvalidETagException( "Given hexString is null." );
        }   
        int length= hexString.length() / 2;
        //check even number of characters
        if ( length * 2 != hexString.length() )
        {
            throw new InvalidETagException( "Given hexString must have even number of characters. The number found: " + hexString.length() );
        }             
        if ( length < 1 || length > 8 )
        {
            throw new InvalidETagException( "ETag length invalid, must be between 1..8 bytes. Given length is: " + length );
        }  
        value= new byte [ length ];
        for ( int i= 0; i < value.length; i++ )
        {
            int index= i * 2;
            int v= Integer.parseInt( hexString.substring( index, index + 2 ), 16 );
            value[i]= (byte) v;
        }
    }

    /**
     * Gets the etag value as byte array.
     * @return Byte array containing the etag value.
     */
    public byte[] asBytes()
    {
        return value.clone();
    }

    /**
     * Converts an etag value to a string containing the hexadecimal representation.
     * @param bytes The etag value.
     * @return The string containing the hexadecimal representation.
     */
    public static String toHexString( byte[] bytes )
    {
        StringBuilder sb= new StringBuilder();
        if ( bytes == null )
        {
            //sb.append( "null" );
        }
        else
        {
            for ( byte b : bytes )
            {
                sb.append( String.format( "%02x", b & 0xFF ) );
            }
        }
        return sb.toString();
    }


    /**
     * Gets the etag value as string containing the hexadecimal representation.
     * @return The string containing the hexadecimal representation.
     */
    @Override
    public String toString()
    {
        return toHexString( value );
    }

    /**
     * Static function that creates etag from byte array.
     * @param etag The byte array containing etag value.
     * @return The etag object created.
     * @throws InvalidETagException when given etag has not a length of 1..8 bytes
     */
    static public ETag create( byte[] etag ) throws InvalidETagException
    {
        return new ETag( etag );
    }

    /**
     * Static function that creates etag from hexadecimal string.
     * @param hexstring
     * @return The etag object created.
     * @throws InvalidETagException when given string does not represent a etag length of 1..8 bytes
     */
    static public ETag create( String hexString ) throws InvalidETagException
    {
        byte[] etag= new byte [hexString.length() / 2];
        for ( int i= 0; i < etag.length; i++ )
        {
            int index= i * 2;
            int v= Integer.parseInt( hexString.substring( index, index + 2 ), 16 );
            etag[i]= (byte) v;
        }
        return new ETag( etag );
    }

    /**
     * Convenience method to create a list of etags.
     * @param etags The List of Byte array to make a list of etags from.
     * @return The list of etags. 
     * @throws InvalidETagException when a given etag has not a length of 1..8 bytes
     */
    static public List< ETag > getList( List< byte[] > etags ) throws InvalidETagException
    {
        LinkedList< ETag > result= new LinkedList< ETag >();
        for ( byte[] etag : etags )
        {
            result.add( new ETag( etag ) );
        }
        return result;
    }


    /**
     * Check a collection of etags whether it contains the etag.
     * @param etags The collection of etags to check.
     * @return True when the etag is found in the collection, otherwise false.
     */
    public boolean isIn( Collection< ETag > etags )
    {
        for ( ETag e : etags )
        {
            if ( this.equals( e ) ) return true;
        }
        return false;
    }
    
    /**
     * Check etag on equality to another etag. 
     * Etags are equal when there byte arrays contain the same sequence of bytes. 
     * @param other The etag to test on equality
     * @return True 
     */
    @Override
    public boolean equals( Object o )
    {
        ETag other = (ETag) o;
        if ( null == other ) throw new NullPointerException( "Etag cannot be compared to null");
        if ( this == other ) return true;
        if ( this.value.length != other.value.length ) return false;
        for ( int i= 0; i < this.value.length && i < other.value.length; i++ )
        {
            if ( this.value[i] != other.value[i] ) return false;
        }
        return true;
    }


    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( ETag other )
    {
        if ( null == other ) throw new NullPointerException( "Etag cannot be compared to null");
        if ( this == other ) return 0;
        if ( this.value.length < other.value.length ) return -1;
        if ( this.value.length > other.value.length ) return 1;
        for ( int i= 0; i < this.value.length && i < other.value.length; i++ )
        {
            if ( this.value[i] < other.value[i] ) return -1;
            if ( this.value[i] > other.value[i] ) return 1;
        }    
        return 0;
    }
    
    @Override 
    public int hashCode()
    {
        
        int hashcode= 0;
        
        for ( int i= 0  ; i < this.value.length ; i++ )
        {
            hashcode+= this.value[i];
        }
        
        return hashcode;
    }
}
