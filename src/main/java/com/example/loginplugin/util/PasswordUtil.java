package com.example.loginplugin.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Hash a password using BCrypt
     */
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * Verify a password against its hash
     */
    public static boolean verifyPassword(String password, String hash) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
    }
}