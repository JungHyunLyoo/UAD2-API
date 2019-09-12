package com.uad2.application.utils;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
 */

import java.math.BigInteger;
import java.security.MessageDigest;

public class EncryptUtil {
    public static String encryptMD5(String source) {
        String md5 = null;
        try {
            MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption algorithm
            mdEnc.update(source.getBytes(), 0, source.length());
            md5 = new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted string
        } catch (Exception ex) {
            return null;
        }
        return md5;
    }
}
