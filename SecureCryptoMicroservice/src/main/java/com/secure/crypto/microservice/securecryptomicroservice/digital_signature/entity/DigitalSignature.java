package com.secure.crypto.microservice.securecryptomicroservice.digital_signature.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DigitalSignature {

    // Acceptable values ecdsa, eddsa
    @JsonProperty("digital_signature_algo")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String asymm_algo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("base64_public_key")
    private String base64EncodedPublicKey;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("base64_private_key")
    private String base64EncodedPrivateKey;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("plaintext_message")
    private String plaintext_message;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("base64_sign")
    private String base64Signature;


    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("verified")
    private boolean verified = false;

    public String getAsymm_algo() {
        return asymm_algo;
    }

    @JsonProperty
    public void setAsymm_algo(String asymm_algo) {

        if(
                !((asymm_algo.toLowerCase().equals("ed-curve")) ||
                (asymm_algo.toLowerCase().equals("ecdsa")))

        ) {
            throw new RuntimeException(asymm_algo + " should be either ed-curve or ecdsa");
        }
        this.asymm_algo = asymm_algo;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getBase64Signature() {
        return base64Signature;
    }

    public void setBase64Signature(String base64Signature) {
        this.base64Signature = base64Signature;
    }

    public String getBase64EncodedPublicKey() {
        return base64EncodedPublicKey;
    }

    public void setBase64EncodedPublicKey(String base64EncodedPublicKey) {
        this.base64EncodedPublicKey = base64EncodedPublicKey;
    }

    public String getBase64EncodedPrivateKey() {
        return base64EncodedPrivateKey;
    }

    public void setBase64EncodedPrivateKey(String base64EncodedPrivateKey) {
        this.base64EncodedPrivateKey = base64EncodedPrivateKey;
    }

    public String getPlaintext_message() {
        return plaintext_message;
    }

    public void setPlaintext_message(String plaintext_message) {
        this.plaintext_message = plaintext_message;
    }


}
