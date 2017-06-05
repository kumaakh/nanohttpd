package org.nanohttpd.protocols.http.auth;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for all authenticators.
 * maintains authorized users and recent nonce.
 * Created by akhil.kumar@gmail.com on 4/5/2017.
 */
public abstract class BaseAuthenticator {
    public class AuthResult{
        public User user;
        public boolean isOK(){return false;}
    }
    Map<String,User> users;
    Map<String,Long> recentNonce;
    int nonceTimeOut=60*1000; //1 minute
    boolean enabled =true;

    public BaseAuthenticator() {
        recentNonce= Collections.synchronizedMap(new LinkedHashMap<String,Long>());
        users = Collections.synchronizedMap(new HashMap<String,User>());
    }

    protected void maintainRecentNonce() {
        long threshold= Calendar.getInstance().getTimeInMillis()-2*nonceTimeOut;
        Iterator<Map.Entry<String, Long>> i = recentNonce.entrySet().iterator();
        while(i.hasNext())
        {
            Map.Entry<String, Long> e=i.next();
            if(e.getValue()<threshold)
            {
                i.remove();
            }
        }
    }

    protected void addNonce(String nonce) {
        synchronized (recentNonce) {
            long now= Calendar.getInstance().getTimeInMillis();
            maintainRecentNonce();
            recentNonce.put(nonce, now);
        }
    }

    protected boolean checkNonce(String nonce)
    {
        synchronized (recentNonce) {
            maintainRecentNonce();
            return recentNonce.containsKey(nonce);
        }
    }

    protected void cacheUser(User user) {
        users.put(user.getUserName(),user);
    }

    public void clear(){
        users.clear();
    }
    public abstract void addUser(User user);
}
