package com.secure.crypto.secure_random;

import java.security.DrbgParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static java.security.DrbgParameters.Capability.PR_AND_RESEED;

public class SecureRandomAPI {

    /*
    Deprecate this API
    This API will generate Cryptographically Secure Random Number Generator.
    It takes into account secured way of seeding the randomizer. It will also take into account underlying operating system being used.

    @param: size == Size of CSPRNG byte array
    @return: Base64 Encoded CSPRNG
     */
    public String generateCSPRNG(int size) {

        byte iv[] = new byte[size];
        SecureRandom secRandom = drbgSecureRandom();
        secRandom.nextBytes(iv);

        return Base64.getEncoder().encodeToString(iv);
    }

    /***
     * Configure SecureRandom object, with most secure configurations. Using NIST approved DRBG mechanism (SHA-256 based), 256 bit security  strength, prediction resistant and reseeding option.
     * @return DRBG  mechanism based, 256 bit security strength, prediction resistant  and reseeding complaint SecureRandom object
     */
    public SecureRandom drbgSecureRandom() {
        SecureRandom drbgSecureRandom = null;
        try {
            drbgSecureRandom = SecureRandom.getInstance("DRBG" , // Uses default configured DRBG mechanism which is usually SHA-256 Hash. Good idea to check in your java.security file.
                    DrbgParameters.instantiation(256, // Security strength, default is 128 as configured in java.security
                                                    PR_AND_RESEED, // prediction resistance, and re-seeding support.
                                                                // Prediction Resistance ==  compromise of the DRBG internal state has no effect on the security of future DRBG outputs.
                                                                // Reseeding == Periodic reseeding, to avoid too many output from a seed
                                                    "any_hardcoded_string".getBytes()));
        } catch (NoSuchAlgorithmException e) { System.out.println("DRBG algorithm for generating CSPRNG is not supported"); }

        return drbgSecureRandom;
    }
}
