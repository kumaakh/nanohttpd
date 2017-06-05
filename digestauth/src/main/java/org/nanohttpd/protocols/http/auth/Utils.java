package org.nanohttpd.protocols.http.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by akhil.kumar@gmail.com on 4/3/2017.
 */

public class Utils {
    private Utils() {
    }

    /**
     * Computes MD5 hash and returns a Hex string of input sting
     *
     * @param inp
     * @return
     */
    public static String computeMD5Hash(String inp) {
        byte[] out = computeDigest("MD5", inp);
        StringBuilder sb = new StringBuilder();
        for (byte b : out) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

   

    /**
     * Computes a digest for bytes of a UTF-8 string using the algorithm
     * examples of algorithm as "SHA-1", "MD5" or any algorithm support by
     * {@link MessageDigest} class.
     * @param algorithm
     * @param inp String of UTF-8 characters
     * @return digest bytes
     */
    public static byte[] computeDigest(String algorithm, String inp) {
        try {
            return computeDigest(algorithm, inp.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("UTF-8 not supported in current system?");
        }
    }

    /**
     * Computes a digest for inBytes using the algorithm
     * examples of algorithm as "SHA-1", "MD5" or any algorithm support by
     * {@link MessageDigest} class.
     * @param algorithm
     * @param inBytes
     * @return digest bytes
     */
    public static byte[] computeDigest(String algorithm, byte[] inBytes) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Algorithm " + algorithm + " not supported in current system");
        }
        md.update(inBytes);
        return md.digest();
    }

    /**
     * Creates a {@link List}
     * @param one
     * @param <T>
     * @return
     */
    public static <T> List<T> listWith(T one)
    {
        List<T> ret = new ArrayList<T>();
        ret.add(one);
        return ret;
    }

    /**
     * Creates a {@link List} using items of an array
     * @param many input array
     * @param <T>
     * @return
     */
    public static <T> List<T> listWith(T... many)
    {
        List<T> ret = new ArrayList<T>();
        Collections.addAll(ret,many);
        return ret;
    }
    /**
     * Joins a collection of strings into a single string with items separated by comma
     * @param many
     * @param <T>
     * @return
     */
    public static <T>String join(Collection<T> many)
    {
        return join(many,",");
    }

    /**
     * Joins a collection of strings into a single string with items separated by "separator"
     * @param many
     * @param separator
     * @param <T>
     * @return
     */
    public static <T>String join(Collection<T> many, String separator)
    {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(T o:many)
        {
            sb.append(sep);
            sep=separator;
            sb.append(o);
        }
        return sb.toString();
    }

   
}
