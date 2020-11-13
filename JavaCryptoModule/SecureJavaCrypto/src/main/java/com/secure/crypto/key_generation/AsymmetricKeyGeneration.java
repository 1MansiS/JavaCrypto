package com.secure.crypto.key_generation;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class AsymmetricKeyGeneration {

    private String EC_CURVE_NAME = "secp384r1" ;
    private String ECC_ALGO = "EC" ;

    private String EDWARD_CURVE = "Ed25519" ;

    /**
     * Generated asymmetric keys to be used with NIST curves
     * @return
     */
    public KeyPair generateECAsymmetricKey() {
        KeyPairGenerator keyPairGenerator = null;

        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ECC_ALGO);
        } catch (NoSuchAlgorithmException e) {System.out.println(ECC_ALGO + " is initialized");}

        ECGenParameterSpec egps = new ECGenParameterSpec(EC_CURVE_NAME);

        try {
            keyPairGenerator.initialize(egps);
        } catch (InvalidAlgorithmParameterException e) {System.out.println(EC_CURVE_NAME + " not able to be initialized");}

        return keyPairGenerator.generateKeyPair();
    }

    /***
     * Generates asymmetric keys to be used with Edward Curves
     * @return
     */
    public KeyPair generateEdAsymmetricKey() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(EDWARD_CURVE); // Unlike any other key generation no extra initialization required such as key size, or actual curve name
        } catch (NoSuchAlgorithmException e) {System.out.println("Exception: While initializing " + EDWARD_CURVE) ;}

        return keyPairGenerator.generateKeyPair();
    }
}
