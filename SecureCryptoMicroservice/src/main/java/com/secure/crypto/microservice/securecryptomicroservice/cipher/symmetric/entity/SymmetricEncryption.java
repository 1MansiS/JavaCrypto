package com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SymmetricEncryption {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="enc_algo")
    private String encAlgo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="base64_symmetric_key")
    private String base64EncodedKey;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="base64_iv")
    private String base64EncodeIV;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "plain_text")
    private String plainText;

    @JsonProperty(value="base64_cipher_text")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String base64EncodedEncryptedCipherText;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="aad")
    private String aad;

    public String getBase64EncodedEncryptedCipherText() {
        return base64EncodedEncryptedCipherText;
    }

    public void setBase64EncodedEncryptedCipherText(String base64EncodedEncryptedCipherText) {
        this.base64EncodedEncryptedCipherText = base64EncodedEncryptedCipherText;
    }

    public String getBase64EncodeIV() {
        return base64EncodeIV;
    }

    public void setBase64EncodeIV(String base64EncodeIV) {
        this.base64EncodeIV = base64EncodeIV;
    }

    public String getBase64EncodedKey() {
        return base64EncodedKey;
    }


    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public void setBase64EncodedKey(String base64EncodedKey) {
        this.base64EncodedKey = base64EncodedKey;
    }

    public String getAad() {
        return aad;
    }

    public void setAad(String aad) {
        this.aad = aad;
    }

    public String getEncAlgo() {
        return encAlgo;
    }

    @JsonProperty
    public void setEncAlgo(String encAlgo) {
        if(!(encAlgo.toLowerCase().equals("aes") || encAlgo.toLowerCase().equals("chacha20"))) {
            throw new RuntimeException("Value " + encAlgo + " is invalid. Only valid values are AES or ChaCha20");
        }
        this.encAlgo = encAlgo.toLowerCase();
    }
}
