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
 *  Exception that is thrown when an attempt is made to construct an invalid ETag object.
 */
public class InvalidETagException extends Exception
{

    /**
     * serial version id
     */
    private static final long serialVersionUID= 1L;

    /**
     * Construct exception with given 
     * @param message
     */
    public InvalidETagException( String message )
    {
        super( message );
    }
}
