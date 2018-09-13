package com.secure.crypto.secure_random;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureRandomAPI {

    /*
    returns Base64 Encoded CSPRNG
     */
    public String generateCSPRNG(int size) {

        byte iv[] = new byte[size];
        SecureRandom secRandom = new SecureRandom() ;
        secRandom.nextBytes(iv); // SecureRandom initialized using self-seeding

        return Base64.getEncoder().encodeToString(iv);
    }
}
