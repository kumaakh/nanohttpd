package org.nanohttpd.protocols.http.auth;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.nanohttpd.protocols.http.request.Method;

/**
 * Created by akhil.kumar@gmail.com on 4/4/2017.
 */

public class DigestAuthenticatorTests {
    DigestAuthenticator authenticator;
    MockSession session;
    @Before
    public void setUp() throws Exception
    {
        authenticator = new DigestAuthenticator("testrealm@host.com");
        authenticator.addUser(new User("Mufasa","Circle Of Life"));

        session = new MockSession(){
            @Override
            public Method getMethod() {
                return Method.GET;
            }
            @Override
            public String getUri() {
                return "/dir/index.html";
            }
        };
    }

    @Test
    public void testDigestAuthAsPerWikipedia() throws  Exception
    {
        String authHeader=
                "Digest username=\"Mufasa\",\n" +
                        "realm=\"testrealm@host.com\",\n" +
                        "nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\"," +
                        "uri=\"/dir/index.html\",\n" +
                        "qop=auth,\n" +
                        "nc=00000001,\n" +
                        "cnonce=\"0a4f113b\",\n" +
                        "response=\"6629fae49393a05397450978507c4ef1\",\n" +
                        "opaque=\"5ccc069c403ebaf9f0171e9517f40e41\"";
        HashMap<String,String> headers= new HashMap<String,String>();
        headers.put(DigestAuthenticator.AUTHORIZATION,authHeader);
        session.setHeaders(headers);
        authenticator.recentNonce.put("dcd98b7102dd2f0e8b11d0f600bfb0c093", Calendar.getInstance().getTimeInMillis());


        BaseAuthenticator.AuthResult result= authenticator.authenticate(session);
        assertTrue(result.isOK());
    }

    @Test
    public void testNoAuth() throws  Exception
    {
        session.setHeaders(new HashMap<String, String>());
        BaseAuthenticator.AuthResult result= authenticator.authenticate(session);
        assertTrue(!result.isOK());
    }

    @Test
    public void testCreateWWWAuthenticateHeader() throws  Exception
    {
        String h= authenticator.createWWWAuthHeader();
        String n=authenticator.recentNonce.keySet().iterator().next();
        assertTrue(h.contains(n));
    }

}
