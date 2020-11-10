package com.secure.crypto.digital_signature;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class ECDigitalSignatureAPI {
    private String ECC_ALGO = "SHA512withECDSA";
    private String ECC_ALGO_KEY_NAME = "EC";

    /***
     * This method digitally signs input plain text message using Edward Curve Ed25519.
     * @param message : Plain text message to be signed
     * @param base64PrivateKey : Generated private key (base64)
     * @return : base64 encoded signature
     */
    public String sign(String message, String base64PrivateKey) {

        Signature sign = null;
        try {
            sign = Signature.getInstance(ECC_ALGO);
        } catch (NoSuchAlgorithmException e) {System.out.println("Exception: While initializing " + ECC_ALGO + " algorithm");}

        PrivateKey privateKey = null;

        try {
            privateKey = KeyFactory.getInstance(ECC_ALGO_KEY_NAME).generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {System.out.println("Exception: While retrieving private key");}

        try {
            sign.initSign(privateKey);
        } catch (InvalidKeyException e) {System.out.println("Exception: While initializing signature object with private  key");}

        try {
            sign.update(message.getBytes());
        } catch (SignatureException e) {System.out.println("Exception: while updating message");}

        byte[] signArray = new byte[0];

        try {
            signArray = sign.sign();
        } catch (SignatureException e) {System.out.println("Exception: while signing");}

        return Base64.getEncoder().encodeToString(signArray);
    }

    /***
     * This method verifies if signature matches with input message.
     * @param message : Input plain text message
     * @param base64PublicKey: Generated public key (base64 encoded), of corresponding private key used to sign  the message
     * @param base64Signature : Base64 encoded signature received while signing
     * @return : If signature matches or not, establishing authenticity, integrity and non-repudiation of the input message.
     */
    public boolean verify(String  message, String base64PublicKey, String base64Signature) {
        Signature verify = null;
        try {
            verify = Signature.getInstance(ECC_ALGO);
        } catch (NoSuchAlgorithmException e) {System.out.println("Exception: while initializing signature object withh " + ECC_ALGO + " algorithn");}

        PublicKey publicKey = null;

        try {
            publicKey = KeyFactory.getInstance(ECC_ALGO_KEY_NAME).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {System.out.println("Exception: while retrieving public  key");}

        try {
            verify.initVerify(publicKey);
        } catch (InvalidKeyException e) {System.out.println("Exception: while preparing for  verification, initializing with public key ");}

        try {
            verify.update(message.getBytes());
        } catch (SignatureException e) {System.out.println("Exception: while updating plain text message");}

        boolean isVerified = false;

        try {
            isVerified = verify.verify(Base64.getDecoder().decode(base64Signature));
        } catch (SignatureException e) {System.out.println("Exception: while verifying");}

        return isVerified;
    }
}
