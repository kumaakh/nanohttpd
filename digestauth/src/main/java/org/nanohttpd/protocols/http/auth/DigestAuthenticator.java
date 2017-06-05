package org.nanohttpd.protocols.http.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Status;


/**
 * Implements digest based authentication for NanoHTTPD
 * https://en.wikipedia.org/wiki/Digest_access_authentication
 * Created by akhil.kumar@gmail.com on 4/4/2017.
 */

public class DigestAuthenticator extends BaseAuthenticator{

    class DigestAuthResult extends AuthResult{
        Status status;
        @Override
        public boolean isOK(){return status==Status.OK;}
    }

    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    public static final String AUTHORIZATION = "authorization";

    String realm;

    public DigestAuthenticator(String realm)
    {
        super();
        this.realm=realm;
    }
    @Override
    public void addUser(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getUserName()).append(':')
                .append(realm).append(':')
                .append(user.getPassword());
        user.setDigest(Utils.computeMD5Hash(sb.toString()));
        cacheUser(user);
    }

    protected String addNewNonce()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID().toString()).append(':').append(realm);
        String nonce=Utils.computeMD5Hash(sb.toString());
        addNonce(nonce);
        return nonce;
    }

    public AuthResult authenticate(IHTTPSession session){
        DigestAuthResult result= new DigestAuthResult();
        result.status=Status.UNAUTHORIZED;
        if(!enabled) {
            result.status=Status.OK;
            return result;
        }
        if(!areCredsProvided(session)) {
            return result; //UNAUTHORIZED
        }
        String authHeader=session.getHeaders().get(AUTHORIZATION);
        StringTokenizer t= new StringTokenizer(authHeader);
        if(t.hasMoreTokens())
        {
            if(!t.nextToken().equals("Digest")) {
                result.status=Status.BAD_REQUEST;
                return result;
            }
        }
        Map<String,String> props= new HashMap<String,String>();
        while(t.hasMoreTokens())
        {
            String k=t.nextToken();
            StringTokenizer kv= new StringTokenizer(k,",\"=");
            String key=null,value=null;
            if(kv.hasMoreTokens()) key=kv.nextToken();
            if(kv.hasMoreTokens()) value=kv.nextToken();
            if(key!=null && value!=null)
                props.put(key,value);
        }
        try{
            if(!realm.equals(props.get("realm")))
                return result;
            if(!"auth".equals(props.get("qop")))
                return result;
            if(!checkNonce(props.get("nonce")))
                return result;

            result.user=checkResponse(props,session);
            if(result.user==null)
                return result;

            result.status=Status.OK;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            result.status=Status.BAD_REQUEST;
        }
        return result;
    }

    private String makeHA2(IHTTPSession s)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(s.getMethod().name())
                .append(":")
                .append(s.getUri());
        return Utils.computeMD5Hash(sb.toString());
    }

    private User checkResponse(Map<String, String> props, IHTTPSession session) {
        String u = props.get("username");
        User user=users.get(u);
        String HA1 = user.getDigest();
        String HA2 = makeHA2(session);
        StringBuilder sb = new StringBuilder();
        sb.append(HA1).append(':')
                .append(props.get("nonce")).append(':')
                .append(props.get("nc")).append(':')
                .append(props.get("cnonce")).append(":auth:")
                .append(HA2);
        String exp_response=Utils.computeMD5Hash(sb.toString());
        if(exp_response.equals(props.get("response")))
        {
            return user;
        }
        return null;
    }
    public String createWWWAuthHeader()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Digest realm=")
                .append('"').append(realm).append("\", ")
                .append("qop=\"auth\", ")
                .append("nonce=\""+addNewNonce()+"\"");

        return sb.toString();
    }

    public boolean areCredsProvided(IHTTPSession session)
    {
        return session.getHeaders().containsKey(AUTHORIZATION);
    }
}
