package com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SymmetricEncryption {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty(value="key_size", required=false)
    private int keySize;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="enc_algo", required=true)
    private String encAlgo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="symmetric_key")
    private String base64EncodedKey;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty(value="iv_size")
    private int ivSize;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="IV")
    private String base64EncodeIV;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="plain_text")
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

    public int getIvSize() {
        return ivSize;
    }

    @JsonProperty
    public void setIvSize(int ivSize) {

        if(ivSize < 12 ) {
            throw new RuntimeException(ivSize + " is too less for an Initialization Vector. Choose something greater than 12 bytes (96 bits)");
        }
        this.ivSize = ivSize;

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

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public String getEncAlgo() {
        return encAlgo;
    }

    @JsonProperty
    public void setEncAlgo(String encAlgo) {
        if(!(encAlgo.toLowerCase().equals("aes") || encAlgo.toLowerCase().equals("chacha20"))) {
            throw new RuntimeException("Value " + encAlgo + " is invalid. Only valid values are AES or ChaCha20");
        }
        this.encAlgo = encAlgo;


    }
}
