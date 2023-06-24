package com.a3fun.pudding.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtils {
    public static final Charset CHARSET = Charset.forName("UTF-8");

    public static final String ALGORITHM_MD5 = "MD5";

    public static final String ALGORITHM_SHA1 = "SHA-1";

    public static final String ALGORITHM_SHA256 = "SHA-256";

    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String md5(byte[] data) {
        return doBytes(ALGORITHM_MD5, data);
    }

    public static String md5(String str) {
        byte[] data;
        data = str.getBytes(CHARSET);
        return doBytes(ALGORITHM_MD5, data);
    }

    public static String md5(InputStream dataIn) {
        return doStream(ALGORITHM_MD5, dataIn);
    }

    public static String sha1(byte[] data) {
        return doBytes(ALGORITHM_SHA1, data);
    }

    public static String sha256(String str) {
        byte[] data;
        data = str.getBytes(CHARSET);
        return doBytes(ALGORITHM_SHA256, data);
    }

    private static String doBytes(String algorithm, byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(data);
        return toBase16(md.digest());
    }

    private static String doStream(String algorithm, InputStream dataIn) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] buf = new byte[1024];
        int numRead = 0;
        try {
            while ((numRead = dataIn.read(buf)) >= 0) {
                md.update(buf, 0, numRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return toBase16(md.digest());
    }

    private static String toBase16(byte[] output) {
        char[] c = new char[output.length * 2];
        for (int i = 0; i < output.length; i++) {
            byte b = output[i];
            c[i * 2] = DIGITS[b >> 4 & 0xF];
            c[i * 2 + 1] = DIGITS[b & 0xF];
        }
        return new String(c);
    }

}
