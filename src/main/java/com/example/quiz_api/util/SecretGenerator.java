package com.example.quiz_api.util;

import java.util.Base64;
import java.security.SecureRandom;

public class SecretGenerator {
    public static void main(String[] args) {
        byte[] secretBytes = new byte[32]; // 256 bits
        new SecureRandom().nextBytes(secretBytes);
        String base64Secret = Base64.getEncoder().encodeToString(secretBytes);
        System.out.println(base64Secret);
    }
}
