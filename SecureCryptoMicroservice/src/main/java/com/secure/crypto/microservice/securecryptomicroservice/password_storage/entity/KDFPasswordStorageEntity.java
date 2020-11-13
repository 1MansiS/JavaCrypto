package com.secure.crypto.microservice.securecryptomicroservice.password_storage.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KDFPasswordStorageEntity {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("salt-size")
    private String saltSize;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("base64-salt")
    private String base64Salt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("base64-kdf-passwd-hash")
    private String base64KDFPasswd;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("passwd")
    private String plainTextPassword;

    public String getSaltSize() {
        return saltSize;
    }

    public void setSaltSize(String saltSize) {
        this.saltSize = saltSize;
    }

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
}
