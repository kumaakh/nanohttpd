package org.nanohttpd.protocols.http.auth;


/**
 * Domain class representing persistable users
 * Created by akhil.kumar@gmail.com on 4/7/2017.
 */

public class User {

   
    String userName;

    String password;


    //transient
    String digest; //for HTTP Digest auth

    public User()
    {
       
    }
    public User(String userName,String password) {
        this.password = password;
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

}
