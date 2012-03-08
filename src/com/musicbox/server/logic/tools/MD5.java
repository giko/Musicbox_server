package com.musicbox.server.logic.tools;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMD5(@NotNull String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            @NotNull BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}