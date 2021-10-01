package com.secure.crypto.microservice.securecryptomicroservice.key_management.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyStoreEntity {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("key-store-password")
    private String keyStorePassword = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("entry-password")
    private String entryPassword = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("alias")
    private String alias = null;


    @JsonProperty("Response")
    private String response_message = null;

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getEntryPassword() {
        return entryPassword;
    }

    public void setEntryPassword(String entryPassword) {
        this.entryPassword = entryPassword;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

}