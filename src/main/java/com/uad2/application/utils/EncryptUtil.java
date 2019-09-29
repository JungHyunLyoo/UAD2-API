package com.uad2.application.utils;


import java.math.BigInteger;
import java.security.MessageDigest;

/*
 * @USER JungHyun
 * @DATE 2019-09-12
 * @DESCRIPTION 암호화 모듈
 */
public class EncryptUtil {
    public static String encryptMD5(String source) {
        String md5;
        try {
            MessageDigest mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(source.getBytes(), 0, source.length());
            md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        } catch (Exception ex) {
            return null;
        }
        return md5;
    }
}
