package com.secure.crypto.cipher.symmetric;

import com.secure.crypto.secure_random.SecureRandomAPI;
import com.secure.crypto.utils.ReadPropertiesFile;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class AESCipherAPI {

    private ReadPropertiesFile readPropertiesFile = new ReadPropertiesFile();

    /***
     * Encrypt using AES-GCM Authenticated Encryption with Associated Data
     * @param base64EncodeKey : Symmetric Key, base64 encoded
     * @param initializationVector : Initialization Vector
     * @param aad : Associated Data
     * @param plainText : Plain Text message to be encrypted
     * @return : Cipher Text, base64 encoded
     */
    public String encrypt(String base64EncodeKey, String initializationVector, String aad, String plainText) {
        Cipher cipher = initializeCipher(base64EncodeKey, initializationVector,  aad, Cipher.ENCRYPT_MODE);

        byte[] cipherArray = null ;
        try {
            cipherArray =  cipher.doFinal(plainText.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {System.out.println("Exception: while trying to perform encryption. Error message " + e.getMessage());}

        return Base64.getEncoder().encodeToString(cipherArray);
    }

    /***
     * Decrypt using AES-GCM Authenticated Encryption with Associated Data
     * @param base64EncodedKey : Symmetric Key, base64 encoded, same as used in encryption
     * @param base64EncodedIV : initialization vector, same as used in encryption
     * @param aad : Associated Data
     * @param cipherText : cipher text, base64 encoded
     * @return : Plain text string
     */
    public String decrypt(String base64EncodedKey, String base64EncodedIV, String aad, String cipherText) {
        Cipher cipher = initializeCipher(base64EncodedKey,base64EncodedIV, aad, Cipher.DECRYPT_MODE );

        byte[] plainTextArray = null;

        try {
            plainTextArray = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        } catch (IllegalBlockSizeException | BadPaddingException e) {System.out.println("Exception: while trying to perform encryption. Error message " + e.getMessage() );}

        return new String(plainTextArray);

    }

    /*
    To avoid code duplication, since Cipher initialization is almost identical for encryption & decryption.
     */
    private Cipher initializeCipher(String base64EncodeKey, String base64EncodedIV, String aad, int mode) {

        Cipher cipher = null ;
        SecureRandomAPI aesRandomizer = new SecureRandomAPI();

        // Initialize GCM Parameters
        // Same base64EncodeKey, IV and GCM Specs are to be used for encryption and decryption.
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(Integer.parseInt(readPropertiesFile.getValue("TAG_BIT_LENGTH")), Base64.getDecoder().decode(base64EncodedIV));

        try {
            cipher = Cipher.getInstance(readPropertiesFile.getValue("AES_ENC_TRANSFORMATION_STRING"));
        } catch (NoSuchAlgorithmException |NoSuchPaddingException e) {System.out.println("Exception: While trying to get Cipher instance with " + readPropertiesFile.getValue("AES_ENC_TRANSFORMATION_STRING") + "transformation. Error message " + e.getMessage()); }

        SecretKey aesKey = new SecretKeySpec(Base64.getDecoder().decode(base64EncodeKey), readPropertiesFile.getValue("DEFAULT_ENC_ALGO"));

        try {
            cipher.init(mode, aesKey, gcmParameterSpec, aesRandomizer.drbgSecureRandom()); // Using DRBG mechanism based CSPRNG
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {System.out.println("Exception: While trying to instantiate Cipher with " +readPropertiesFile.getValue("AES_ENC_TRANSFORMATION_STRING") + "transformation for encryption. Error message " + e.getMessage()); }

        cipher.updateAAD(aad.getBytes()); // add AAD tag data before encrypting or decryption

        return cipher;
    }
}
