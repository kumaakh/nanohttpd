package org.nanohttpd.protocols.http.auth;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.content.CookieHandler;
import org.nanohttpd.protocols.http.request.Method;

/**
 * Created by akhil.kumar@gmail.com on 4/4/2017.
 */

public class MockSession implements IHTTPSession {
    Map<String, String> headers;
    public void setHeaders(Map<String, String> headers){
        this.headers=headers;
    }
    public void execute() throws IOException {

    }

    public CookieHandler getCookies() {
        return null;
    }


    
    public Map<String, String> getHeaders() {
        return headers;
    }

    
    public InputStream getInputStream() {
        return null;
    }

    
    public Method getMethod() {
        return null;
    }

    
    public Map<String, String> getParms() {
        return null;
    }

    
    public Map<String, List<String>> getParameters() {
        return null;
    }

    
    public String getQueryParameterString() {
        return null;
    }

    
    public String getUri() {
        return null;
    }

    
    public void parseBody(Map<String, String> files) throws IOException, NanoHTTPD.ResponseException {

    }

    
    public String getRemoteIpAddress() {
        return null;
    }

    
    public String getRemoteHostName() {
        return null;
    }
}
