package com.tudou.android.fw.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digest {
    private static final String TAG = Digest.class.getSimpleName();
    private static final String UTF_8 = "utf-8";
    
    private static MessageDigest digester;
    
    static {
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            TudouLog.e(TAG, "NoSuchAlgorithmException", e);
        }
    }
    
    public static String digest(String ... datas) {
        String digest = "";
        try {
            for (String data: datas) {
                if (null != data) {
                    digester.update(data.getBytes(UTF_8));
                }
            }
            byte[] bytes = digester.digest();
            for (byte b : bytes) {
                digest += Integer.toHexString(b & 0xFF);
            }
        } catch (UnsupportedEncodingException e) {
            TudouLog.e(TAG, "UnsupportedEncodingException", e);
        }
        
        return digest;
    }
    
    private Digest(){}// Utility class, do not instantiate.
}
