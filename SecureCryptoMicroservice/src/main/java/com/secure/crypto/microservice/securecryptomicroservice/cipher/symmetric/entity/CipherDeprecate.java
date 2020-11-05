package com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.entity;

public class CipherDeprecate {

    /*
    Base64 Encoded SecretKey
     */
    private String key;
    /*
    Base64 Encoded Initialization Vector
     */
    private String initializationVector;
    /*
    PlainText, AAD value
     */
    private String aad;
    /*
    Plain text to be encrypted
     */
    private String data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInitializationVector() {
        return initializationVector;
    }

    public void setInitializationVector(String initializationVector) {
        this.initializationVector = initializationVector;
    }

    public String getAad() {
        return aad;
    }

    public void setAad(String aad) {
        this.aad = aad;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
