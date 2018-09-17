package com.secure.crypto.digital_signature;

import com.secure.crypto.utils.PropertiesFile;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class DigitalSignatureAPI {

    private PropertiesFile propertiesFile = new PropertiesFile();

    /*
    @param data : plain text data
    @param privateKey: Base64 encoded Private Key

    @return Base64 Encoded digital signature
     */
    public String sign(String data, String encodedPrivateKey) {

        Security.addProvider(new BouncyCastleProvider());

        Signature sign = null;

        try {
            sign = Signature.getInstance(propertiesFile.getPropertyValue("DIGITAL_KEY_ALGO"),propertiesFile.getPropertyValue("DIGITAL_SIGNATURE_PROVIDER"));
        } catch (NoSuchAlgorithmException|NoSuchProviderException e) {System.out.println("Exception: " +propertiesFile.getPropertyValue("DIGITAL_KEY_ALGO")+ "not supported by provider. Error message " + e.getMessage() ); System.exit(0);}

        PrivateKey privateKey = null;

        try {
            privateKey = KeyFactory.getInstance(propertiesFile.getPropertyValue("ASYMMETRIC_ALGO")).generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(encodedPrivateKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException  e) {System.out.println("Exception: Can't retrieve private key. Error message " + e.getMessage());System.exit(0);}

        System.out.println("Public Key algorithm  " + privateKey.getAlgorithm() + " format == " + privateKey.getFormat());

        try {
            sign.initSign(privateKey);
        } catch (InvalidKeyException e) {System.out.println("Exception: Key is invalid. Error message. Error message " + e.getMessage());  System.exit(0);}

        try {
            sign.update(data.getBytes());
        } catch (SignatureException e) {System.out.print("Exception: Problem with data to be signed. Error message " + e.getMessage()); System.exit(0);}

        byte[] signArray = new byte[0];

        try {
            signArray = sign.sign();
        } catch (SignatureException e) {System.out.println("Exception: While generating signature. Error message " + e.getMessage()); System.exit(0);}

        return Base64.getEncoder().encodeToString(signArray);

    }


    /*
    @param data: plain text data to be verified
    @param encodedPublicKey : Base64Encoded Public Key
    @param encodedSignature: Base64 encoded digital signature to be verified
     */
    public boolean verify(String data, String encodedPublicKey, String encodedSignature) {

        Security.addProvider(new BouncyCastleProvider());

        Signature verify = null;

        try {
            verify = Signature.getInstance(propertiesFile.getPropertyValue("DIGITAL_KEY_ALGO"),propertiesFile.getPropertyValue("DIGITAL_SIGNATURE_PROVIDER"));
        } catch (NoSuchAlgorithmException|NoSuchProviderException e) {System.out.println("Exception: " +propertiesFile.getPropertyValue("DIGITAL_KEY_ALGO")+ "not supported by provider. Error message " + e.getMessage() ); System.exit(0);}


        PublicKey publicKey = null;



        try {
            publicKey = KeyFactory.getInstance(propertiesFile.getPropertyValue("ASYMMETRIC_ALGO")).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(encodedPublicKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException  e) {System.out.println("Exception: Can't retrieve public key " + e.getMessage());System.exit(0);}

        System.out.println("Public Key algorithm  " + publicKey.getAlgorithm() + " format == " + publicKey.getFormat());



        try {
            verify.initVerify(publicKey);
        } catch (InvalidKeyException e) {System.out.println("Exception: Key is invalid. Error message " + e.getMessage());  System.exit(0);}

        try {
            verify.update(data.getBytes());
        } catch (SignatureException e) {System.out.print("Exception: Problem with data to be signed. Error message " + e.getMessage()); System.exit(0);}

        boolean isVerified = false;
        try {
            isVerified = verify.verify(Base64.getDecoder().decode(encodedSignature.trim()));
        } catch (SignatureException e) {System.out.println("Exception: While verifying signature " + e.getMessage()); System.exit(0);}

        return isVerified;

    }
}
