package sanger.team16.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;

public class WSSecurityInterceptor extends WSS4JInInterceptor
{
    public WSSecurityInterceptor() throws UnsupportedCallbackException, IOException {
        Map<String,Object> inProps= new HashMap<String,Object>();
        inProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        inProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ServerPasswordCallback.class.getName());

        this.setProperties(inProps);
    }
}
