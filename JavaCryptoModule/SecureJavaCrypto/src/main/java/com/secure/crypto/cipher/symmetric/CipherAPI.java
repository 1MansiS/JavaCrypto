package com.secure.crypto.cipher.symmetric;

import com.secure.crypto.utils.PropertiesFile;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class CipherAPI {

    private PropertiesFile propertiesFile = new PropertiesFile();

    /*
    Exported API to perform Encryption

    @param key == Base64 endoded SecretKey
    @param initializationVector == Base64 encoded CSPRNG
    @param aad = Server Name
    @plainText  == Text to be encrypted

    @return Base64 encoded encrypted data

     */
    public String encrypt(String key, String initializationVector, String aad, String plainText) {
        Cipher cipher = initializeCipher(key, initializationVector,  aad, Cipher.ENCRYPT_MODE);

        byte[] cipherArray = null ;
        try {
            cipherArray =  cipher.doFinal(plainText.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {System.out.println("Exception: while trying to perform encryption. Error message " + e.getMessage());}

        return Base64.getEncoder().encodeToString(cipherArray);
    }

    /*
    Exported API to perform Decryption
    @param key == Base64 endoded SecretKey
    @param intializationvector == Base64 encoded CSPRNG
    @param aad = Server Name
    @param cipherText ==  Base64 encoded Cipher Text

    @return: Plain Text Data
     */
    public String decrypt(String key, String intializationvector, String aad, String cipherText) {
        Cipher cipher = initializeCipher(key,intializationvector, aad, Cipher.DECRYPT_MODE );

        byte[] plainTextArray = null;

        try {
            plainTextArray = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        } catch (IllegalBlockSizeException | BadPaddingException e) {System.out.println("Exception: while trying to perform encryption. Error message " + e.getMessage() );}

        return new String(plainTextArray);

    }

    /*
    To avoid code duplication, since Cipher initialization is almost identical for encryption & decryption.
     */
    private Cipher initializeCipher(String key, String intializationVector, String aad, int mode) {

        // Initialize GCM Parameters
        // Same key, IV and GCM Specs are to be used for encryption and decryption.
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(Integer.parseInt(propertiesFile.getPropertyValue("TAG_BIT_LENGTH")), Base64.getDecoder().decode(intializationVector));

        Cipher cipher = null ;

        try {
            cipher = Cipher.getInstance(propertiesFile.getPropertyValue("ALGO_TRANSFORMATION_STRING"));
        } catch (NoSuchAlgorithmException |NoSuchPaddingException e) {System.out.println("Exception: While trying to get Cipher instance with " + propertiesFile.getPropertyValue("ALGO_TRANSFORMATION_STRING") + "transformation. Error message " + e.getMessage()); System.exit(1);}

        SecretKey aesKey = getSecretKeyFromByteArray(Base64.getDecoder().decode(key));

        try {
            cipher.init(mode, aesKey, gcmParameterSpec, new SecureRandom());
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {System.out.println("Exception: While trying to instantiate Cipher with " + propertiesFile.getPropertyValue("ALGO_TRANSFORMATION_STRING") + "transformation for encryption. Error message " + e.getMessage()); System.exit(1);}

        cipher.updateAAD(aad.getBytes()); // add AAD tag data before encrypting or decryption

        return cipher;
    }

    private SecretKey getSecretKeyFromByteArray(byte[] encodedSecretKey) {
        SecretKey secretKey = new SecretKeySpec(encodedSecretKey, 0 , encodedSecretKey.length, propertiesFile.getPropertyValue("ENC_ALGO"));

        return secretKey;
    }
}
