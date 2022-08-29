package com.secure.crypto.microservice.securecryptomicroservice.mac.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hmac {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("message")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("base64-symmetric-key")
    private String base64SymmetricKey;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="mac")
    private String mac;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBase64SymmetricKey() {
        return base64SymmetricKey;
    }

    public void setBase64SymmetricKey(String base64SymmetricKey) {
        this.base64SymmetricKey = base64SymmetricKey;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
