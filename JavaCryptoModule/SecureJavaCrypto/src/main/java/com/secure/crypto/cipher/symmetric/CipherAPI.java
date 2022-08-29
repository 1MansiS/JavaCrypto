package com.secure.crypto.cipher.symmetric;

import java.util.Base64;

public class CipherAPI {
    private AESCipherAPI aesCipherAPI = new AESCipherAPI();
    private ChaChaCipherAPI chaChaCipherAPI = new ChaChaCipherAPI();

    public String encrypt(String base64EncodeKey, String base64EncodedInitializationVector, String aad, String plainText) {
        String base64EncoderCipherText = "";

        if(Base64.getDecoder().decode(base64EncodedInitializationVector).length == 16) { // IV == 16 bytes for AES
            base64EncoderCipherText = aesCipherAPI.encrypt(
                    base64EncodeKey,
                    base64EncodedInitializationVector,
                    aad,
                    plainText
            );
        } else {
            base64EncoderCipherText = chaChaCipherAPI.encrypt(base64EncodeKey,
                    base64EncodedInitializationVector,
                    plainText);
        }

        return base64EncoderCipherText;
    }

    public String decrypt(String base64EncodedKey, String base64EncodedIV, String aad, String base64EncodedCipherText) {
        String plainText = "";

        if(Base64.getDecoder().decode(base64EncodedIV).length == 16) { // IV == 16 bytes for AES
            plainText = aesCipherAPI.decrypt(
                    base64EncodedKey,
                    base64EncodedIV,
                    aad,
                    base64EncodedCipherText
            );
        } else {
            plainText = chaChaCipherAPI.decrypt(
                    base64EncodedKey,
                    base64EncodedIV,
                    base64EncodedCipherText
            );
        }

        return plainText;
    }
}
