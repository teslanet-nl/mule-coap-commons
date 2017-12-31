package nl.teslanet.mule.connectors.coap.exceptions;

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
