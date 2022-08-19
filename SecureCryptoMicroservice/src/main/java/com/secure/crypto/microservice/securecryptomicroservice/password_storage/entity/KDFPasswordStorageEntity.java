package com.secure.crypto.microservice.securecryptomicroservice.password_storage.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KDFPasswordStorageEntity {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("base64-salt")
    private String base64Salt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("base64-kdf-passwd-hash")
    private String base64KDFPasswd;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("passwd")
    private String plainTextPassword;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("kdf-algo")
    private String passwdHashingAlgo;

    public String getBase64Salt() {
        return base64Salt;
    }

    public void setBase64Salt(String base64Salt) {
        this.base64Salt = base64Salt;
    }

    public String getBase64KDFPasswd() {
        return base64KDFPasswd;
    }

    public void setBase64KDFPasswd(String base64KDFPasswd) {
        this.base64KDFPasswd = base64KDFPasswd;
    }

    public String getPlainTextPassword() {
        return plainTextPassword;
    }

    public void setPlainTextPassword(String plainTextPassword) {
        this.plainTextPassword = plainTextPassword;
    }

    public String getPasswdHashingAlgo() {
        return passwdHashingAlgo;
    }

    @JsonProperty
    public void setPasswdHashingAlgo(String passwdHashingAlgo) {
        if(!(passwdHashingAlgo.toLowerCase().equals("argon2") || passwdHashingAlgo.toLowerCase().equals("pbkdf2") || passwdHashingAlgo.toLowerCase().equals("bcrypt") || passwdHashingAlgo.toLowerCase().equals("scrypt")   )) {
            throw new RuntimeException(passwdHashingAlgo + " value is invalid. Only acceptable values are Argon2, bcrypt, scrypt or PBKDF2");
        }
        this.passwdHashingAlgo = passwdHashingAlgo;
    }

}
