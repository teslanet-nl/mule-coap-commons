/*******************************************************************************
 * Copyright (c) 2018 (teslanet.nl) Rogier Cobben.
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


/**
 *  Exception that is thrown when an attempt is made to construct an option using an invalid value.
 */
public class InvalidOptionValueException extends Exception
{

    /**
     * serial version id
     */
    private static final long serialVersionUID= 1L;

    /**
     * Construct exception with given message.
     * @param message Description of the exception
     */
    public InvalidOptionValueException( String message )
    {
        super( message );
    }
    
    /**
     * Construct exception with given message.
     * @param message description of the exception
     * @param cause underlying cause
     */
    public InvalidOptionValueException( String message, Throwable cause )
    {
        super( message, cause );
    }
    
    /**
     * Construct exception for an option with given message.
     * @param optionName name of the option the exception occurred on
     * @param message description of the exception
     */
    public InvalidOptionValueException( String optionName, String message )
    {
        super( "Value of option " + optionName + " is invalid, " + message );
    }
    
    /**
     * Construct exception for an option with given message.
     * @param optionName name of the option the exception occurred on
     * @param message description of the exception
     * @param cause underlying cause
     */
    public InvalidOptionValueException( String optionName, String message, Throwable cause )
    {
        super( "Value of option " + optionName + " is invalid, " + message, cause );
    }
        
}
