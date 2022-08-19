package com.secure.crypto.microservice.securecryptomicroservice.message_digest.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Digest {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("message")
    private String message;



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="hashing-algo")
    private String hashingAlgo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("hash")
    private String hash;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashingAlgo() {
        return hashingAlgo;
    }

    public void setHashingAlgo(String hashingAlgo) {
        this.hashingAlgo = hashingAlgo;
    }

}
