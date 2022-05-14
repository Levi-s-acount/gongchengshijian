package com.fifteen.webproject.utils;


import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SecretUtils {


    /**
     * Salt for encryption
     */
    public static final String ENCRYPTION_SALT = "gongchengshijian$%%^";

    /***
     * Using Apache's tool class to realize SHA-256 encryption
     * @param str String to be encrypted
     * @return Encrypted message
     */
    public static String getSHA256Str(String str){
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

    public static String encrypt(String s) {
        String result = s + ENCRYPTION_SALT;
        for (int i = 0; i < 10; i++) {
            result = getSHA256Str(result);
        }
        return result;
    }


}
