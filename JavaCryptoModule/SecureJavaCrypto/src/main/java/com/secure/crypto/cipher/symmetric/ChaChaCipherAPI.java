package com.secure.crypto.cipher.symmetric;


import com.secure.crypto.secure_random.SecureRandomAPI;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

public class ChaChaCipherAPI {

    private String CHACHA20_POLY1305_TRANSFORMATION_STRING = "ChaCha20-Poly1305/None/NoPadding";
    private String CHACHA20_ENC_ALGO = "ChaCha20";

    /***
     * Encrypt using ChaCha20-Poly1305 Authenticated Encryption
     * @param base64EncodeKey : Symmetric Key, base64 encoded
     * @param base64EncodeIV : 12 byte initialization vector
     * @param plainText : Plain Text message to be encoded
     * @return : Cipher Text, base64 encoded
     */
    public String encrypt(String base64EncodeKey, String base64EncodeIV, String plainText) {
        Cipher chachaCipher = initChaChaCipher(Cipher.ENCRYPT_MODE,base64EncodeKey,base64EncodeIV);
        byte[] cipherTextArray = null;
        try {
            cipherTextArray = chachaCipher.doFinal(plainText.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {System.out.println("Issues in encrypting with  " + CHACHA20_POLY1305_TRANSFORMATION_STRING + " " + e.getMessage()) ;}
        return Base64.getEncoder().encodeToString(cipherTextArray);
    }

    /***
     * Decrypt using ChaCha20-Poly1305 Authenticated Decryption
     * @param base64EncodeKey : Symmetric Key, base64 encoded, same as used in encryption
     * @param base64EncodeIV : 12 byte initialization vector, same as used in encryption
     * @param base64EncodedCipherText : cipher text, base64 encoded
     * @return : Plain text string
     */
    public String decrypt(String base64EncodeKey, String base64EncodeIV, String base64EncodedCipherText) {
        Cipher chachaCipher = initChaChaCipher(Cipher.DECRYPT_MODE, base64EncodeKey, base64EncodeIV);
        byte[] plainTextArray = null;
        try {
            plainTextArray = chachaCipher.doFinal(Base64.getDecoder().decode(base64EncodedCipherText));
        } catch (IllegalBlockSizeException | BadPaddingException e) {System.out.println("Issues decrypting with " + CHACHA20_POLY1305_TRANSFORMATION_STRING + " " + e.getMessage() ); }
        return new String(plainTextArray);
    }

    /***
     * Since initializing  a cipher is similar for encryption and decryption, created a common method for code reuse
     * @param mode
     * @param base64EncodeKey
     * @param base64EncodeIV
     * @return
     */
    private Cipher initChaChaCipher(int mode, String base64EncodeKey, String base64EncodeIV) {

        Cipher cipher = null;

        SecureRandomAPI chachaRandomizer = new SecureRandomAPI();
        try {
            cipher = Cipher.getInstance(CHACHA20_POLY1305_TRANSFORMATION_STRING);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {System.out.println(CHACHA20_POLY1305_TRANSFORMATION_STRING + " has some issues") ;}

        // Transparent specification for configuring 12 byte nonce
        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(base64EncodeIV)) ;

        // Getting Symmetric Key spec from base64 encoded string
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(base64EncodeKey),CHACHA20_ENC_ALGO);

        try {
            cipher.init(mode, secretKeySpec, ivParameterSpec, chachaRandomizer.drbgSecureRandom()); // Using DRBG mechanism based randomizer
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {System.out.println("Issues in initializing " + CHACHA20_POLY1305_TRANSFORMATION_STRING + " " + e.getMessage()); }

        return cipher;
    }

}
