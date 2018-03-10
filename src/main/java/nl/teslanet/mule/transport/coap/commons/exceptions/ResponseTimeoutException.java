/*******************************************************************************
 * Copyright (c) 2018 (teslanet.nl) Rogier Cobben.
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

package nl.teslanet.mule.transport.coap.commons.exceptions;

import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.Type;

public class ResponseTimeoutException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID= 1L;

    public ResponseTimeoutException( String uri, Type type, Code requestCode )
    {
        super( "timeout while waiting for response on request: " + type.toString() + "-" + requestCode.toString() + " uri: " + uri );
    }


}
