package com.secure.crypto.secure_random;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureRandomAPI {

    /*
    This API will generate Cryptographically Secure Random Number Generator.
    It takes into account secured way of seeding the randomizer. It will also take into account underlying operating system being used.

    @param: size == Size of CSPRNG byte array
    @return: Base64 Encoded CSPRNG
     */
    public String generateCSPRNG(int size) {

        byte iv[] = new byte[size];
        SecureRandom secRandom = new SecureRandom() ;
        secRandom.nextBytes(iv); // SecureRandom initialized using self-seeding

        return Base64.getEncoder().encodeToString(iv);
    }
}
