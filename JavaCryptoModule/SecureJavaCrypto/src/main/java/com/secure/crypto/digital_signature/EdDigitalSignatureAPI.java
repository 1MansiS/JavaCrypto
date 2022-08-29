package com.secure.crypto.digital_signature;

import com.secure.crypto.utils.ReadPropertiesFile;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EdDigitalSignatureAPI {
    private ReadPropertiesFile readPropertiesFile = new ReadPropertiesFile();

    private String EDDSA_ALGO = readPropertiesFile.getValue("EDDSA_ALGO");

    /***
     * This method digitally signs input plain text message using Edward Curve Ed25519.
     * @param message : Plain text message to be signed
     * @param base64PrivateKey : Generated private key (base64)
     * @return : base64 encoded signature
     */
    public String sign(String message, String base64PrivateKey) {

        Signature sign = null;
        try {
            sign = Signature.getInstance(EDDSA_ALGO);
        } catch (NoSuchAlgorithmException e) {System.out.println("Problem initializing " + EDDSA_ALGO);}

        PrivateKey privateKey = null;
        try {
            privateKey = KeyFactory.getInstance(EDDSA_ALGO).generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {System.out.println("Exception: While retrieving private key using PKCS8 specs for " + EDDSA_ALGO);}

        try {
            sign.initSign(privateKey);
        } catch (InvalidKeyException e) {System.out.println("Exception: While signing using " + EDDSA_ALGO);}

        try {
            sign.update(message.getBytes());
        } catch (SignatureException e) {System.out.println("Exception: While updating plaintext message ");}

        byte[] signArray = new byte[0];

        try {
            signArray = sign.sign();
        } catch (SignatureException e) {System.out.println("Exception: while signing using " + EDDSA_ALGO);}

        return Base64.getEncoder().encodeToString(signArray);
    }

    /***
     * This method verifies if signature matches with input message.
     * @param message : Input plain text message
     * @param base64PublicKey: Generated public key (base64 encoded), of corresponding private key used to sign  the message
     * @param base64Signature : Base64 encoded signature received while signing
     * @return : If signature matches or not, establishing authenticity, integrity and non-repudiation of the input message.
     */
    public boolean verify(String message, String base64PublicKey, String base64Signature) {

        Signature verify = null;
        try {
            verify = Signature.getInstance(EDDSA_ALGO);
        } catch (NoSuchAlgorithmException e) {System.out.println("Exception: While initializing " + EDDSA_ALGO);}

        PublicKey publicKey = null;
        try {
            publicKey = KeyFactory.getInstance(EDDSA_ALGO).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {System.out.println("Exception: While retrieving public key using X509 spec for " + EDDSA_ALGO);e.printStackTrace();}

        try {
            verify.initVerify(publicKey);
        } catch (InvalidKeyException e) {System.out.println("Exception: While initializing with Public Key for usage with "+ EDDSA_ALGO);}

        try {
            verify.update(message.getBytes());
        } catch (SignatureException e) {System.out.println("Exception : While updating with message");}

        boolean isVerified = false;

        try {
            isVerified = verify.verify(Base64.getDecoder().decode(base64Signature));
        } catch (SignatureException e) {System.out.println("Exception: While, verifying signature");}

        return isVerified;
    }
}
