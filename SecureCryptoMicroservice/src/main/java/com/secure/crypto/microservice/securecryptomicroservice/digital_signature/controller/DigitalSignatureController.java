package com.secure.crypto.microservice.securecryptomicroservice.digital_signature.controller;

import com.secure.crypto.digital_signature.ECDigitalSignatureAPI;
import com.secure.crypto.digital_signature.EdDigitalSignatureAPI;
import com.secure.crypto.key_generation.AsymmetricKeyGeneration;
import com.secure.crypto.microservice.securecryptomicroservice.digital_signature.entity.DiigitalSignature;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.util.Base64;

@RestController
public class DigitalSignatureController {

    AsymmetricKeyGeneration asymmetricKeyGeneration = new AsymmetricKeyGeneration();
    EdDigitalSignatureAPI edDigitalSignatureAPI = new EdDigitalSignatureAPI();
    ECDigitalSignatureAPI ecDigitalSignatureAPI = new ECDigitalSignatureAPI();

    /***
     * Generated Asymmetric Keys. Specify the  digital signature  algorithm to be  used. Only accepted values are ed-curve (edward curves)  or eddsa(NIST curves)
     * Sample Request: curl -X POST 'http://localhost:8080/generateAsymmetricKey' -H  "Content-Type: application/json" -d '{"asymm_algo":"ed-curve"}' | json_pp
     * @param diigitalSignature  :  Expects asymm_algo with only expects values of ed-curve or ecdsa
     * @return : base64 encoded public and private keys
     * {
     *    "asymm_algo" : "ed-curve",
     *    "base64_public_key" : "MCowBQYDK2VwAyEAW5CKDO5xEO5EVVIcMeBFJ0w4nI6MDmWjWEHgZ4Zqeoc=",
     *    "base64_private_key" : "MC4CAQAwBQYDK2VwBCIEIJtmYm8YaxQW50/LI8Uhf3ft1uUB7kKHbK7jF0Ze1SqF"
     * }
     */
    @PostMapping(value="/generateAsymmetricKey",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE
                )
    public DiigitalSignature generateAsymmetricKey(@RequestBody DiigitalSignature diigitalSignature) {
        KeyPair keyPair = null;

        if (diigitalSignature.getAsymm_algo().compareToIgnoreCase("ed-curve") == 0) { // Its asking to use Edward Curves
            keyPair = asymmetricKeyGeneration.generateEdAsymmetricKey();
        } else { // Use a safe NIST curves (ecdsa)
            keyPair = asymmetricKeyGeneration.generateECAsymmetricKey();
        }

        diigitalSignature.setBase64EncodedPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        diigitalSignature.setBase64EncodedPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));

        return diigitalSignature;
    }

    /***
     * This endpoint, generated the digital signature for the message passed
     * Sample Request: curl -X POST 'http://localhost:8080/sign' -H  "Content-Type: application/json" -d '{"asymm_algo":"ed-curve","base64_private_key":"MC4CAQAwBQYDK2VwBCIEIJtmYm8YaxQW50/LI8Uhf3ft1uUB7kKHbK7jF0Ze1SqF","plaintext_message":"Hello World!"}' | json_pp
     * @param diigitalSignature: Pass the asymm_algo, base64_private_key and plaintext_message
     * @return : Base 64 signature value:
     *{
     *    "plaintext_message" : "Hello World!",
     *    "asymm_algo" : "ed-curve",
     *    "base64_sign" : "FOh5uarkS3MMdkraAUJywSK8M/SXQbgbOjjze0zgsDzQ0QqH3++dbeev/WPdKdKXQwRDzY0v8rUKP1rDAL0MBQ==",
     *    "base64_private_key" : "MC4CAQAwBQYDK2VwBCIEIJtmYm8YaxQW50/LI8Uhf3ft1uUB7kKHbK7jF0Ze1SqF"
     * }
     */
    @PostMapping(value="/sign",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
        )
    public DiigitalSignature sign(@RequestBody DiigitalSignature diigitalSignature) {


        if(diigitalSignature.getAsymm_algo().compareToIgnoreCase("ed-curve") == 0) { // Use Edward Curves
            diigitalSignature.setBase64Signature(
                    edDigitalSignatureAPI.sign(
                            diigitalSignature.getPlaintext_message(),
                            diigitalSignature.getBase64EncodedPrivateKey())
            );
        } else { // Use NIST curves
            diigitalSignature.setBase64Signature(
                    ecDigitalSignatureAPI.sign(
                            diigitalSignature.getPlaintext_message(),
                            diigitalSignature.getBase64EncodedPrivateKey()
                    )
            );
        }


        return diigitalSignature;
    }

    /***
     * This endpoint, verifies the digital signature passed
     * Samepl Request: curl -X POST 'http://localhost:8080/verify' -H  "Content-Type: application/json" -d '{"asymm_algo":"ed-curve","plaintext_message":"Hello World!","base64_public_key":"MCowBQYDK2VwAyEAW5CKDO5xEO5EVVIcMeBFJ0w4nI6MDmWjWEHgZ4Zqeoc=","base64_sign":"FOh5uarkS3MMdkraAUJywSK8M/SXQbgbOjjze0zgsDzQ0QqH3++dbeev/WPdKdKXQwRDzY0v8rUKP1rDAL0MBQ=="}' | json_pp
     * @param diigitalSignature: Expects: asymm_algo, base64_public_key, plaintext_message and base64_sign
     * @return: If the signature is verified. Look for the value of verified  key in response json string.
     * {
     *    "base64_sign" : "FOh5uarkS3MMdkraAUJywSK8M/SXQbgbOjjze0zgsDzQ0QqH3++dbeev/WPdKdKXQwRDzY0v8rUKP1rDAL0MBQ==",
     *    "plaintext_message" : "Hello World!",
     *    "asymm_algo" : "ed-curve",
     *    "verified" : true,
     *    "base64_public_key" : "MCowBQYDK2VwAyEAW5CKDO5xEO5EVVIcMeBFJ0w4nI6MDmWjWEHgZ4Zqeoc="
     * }
     */
    @PostMapping(value="/verify",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DiigitalSignature verify(@RequestBody DiigitalSignature diigitalSignature) {

        if(diigitalSignature.getAsymm_algo().compareToIgnoreCase("ed-curve") == 0){ // Use Edward Curves
            diigitalSignature.setVerified(
                    edDigitalSignatureAPI.verify(
                            diigitalSignature.getPlaintext_message(),
                            diigitalSignature.getBase64EncodedPublicKey(),
                            diigitalSignature.getBase64Signature()
                    )
            );
        } else { // Use NIST curves
            diigitalSignature.setVerified(
                    ecDigitalSignatureAPI.verify(diigitalSignature.getPlaintext_message(),
                            diigitalSignature.getBase64EncodedPublicKey(),
                            diigitalSignature.getBase64Signature()
                    )
            );
        }

        return diigitalSignature;
    }
}

/*
Workflow using ecdsa algorithm(NIST curves):

Step 1: Generating Keys:

Sample Request: curl -X POST 'http://localhost:8080/generateAsymmetricKey' -H  "Content-Type: application/json" -d '{"asymm_algo":"eddsa"}' | json_pp
Response: {
   "asymm_algo" : "eddsa",
   "base64_public_key" : "MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAEnQMUFAvChWOt+pf8f5miYpdpccyMFftjNDQouNjEnoA2TrUM67l72VGIenGUZu14zBqx+ZseqhQB1DrfyTmgWRcVr2RhKkv5DfC9TJBQSU9uoQYvCzJ7Z45fpYpwIa8g",
   "base64_private_key" : "ME4CAQAwEAYHKoZIzj0CAQYFK4EEACIENzA1AgEBBDAOTIPhjObzlwUKcvoyqK6BFmvE03Ks+vThtOJQjt9+FViqMn4txAQYweIAIwepUjg="
}

Step 2: Sign

Sample Request: curl -X POST 'http://localhost:8080/sign' -H  "Content-Type: application/json" -d '{"asymm_algo":"ed-curve","base64_private_key":"ME4CAQAwEAYHKoZIzj0CAQYFK4EEACIENzA1AgEBBDDT9/95QqyOTT2J3brAwt41BTqcB2XLVdO1BbOFGt/1pAlStmiw525ZMpLugT7vPd4","plaintext_message":"Hello World!"}' | json_pp
Response: {
   "base64_private_key" : "ME4CAQAwEAYHKoZIzj0CAQYFK4EEACIENzA1AgEBBDAOTIPhjObzlwUKcvoyqK6BFmvE03Ks+vThtOJQjt9+FViqMn4txAQYweIAIwepUjg",
   "asymm_algo" : "eddsa",
   "base64_sign" : "MGUCME4pcfKJgBebtxJKBUrMQREWX0YVwssWdSxnT7AbG7S4Su8gjQdi/rlmfsp/28xLWQIxANxagG7zeO1ZwTnrryD5EC0p06WGqS+nU8YqRJg5VOxO1sgeDKHeIlTF4OUOb6fjig==",
   "plaintext_message" : "Hello World!!!!"
}

Step 3: Verify

Sample Request: curl -X POST 'http://localhost:8080/verify' -H  "Content-Type: application/json" -d '{"asymm_algo":"eddsa","base64_public_key":"MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAEnQMUFAvChWOt+pf8f5miYpdpccyMFftjNDQouNjEnoA2TrUM67l72VGIenGUZu14zBqx+ZseqhQB1DrfyTmgWRcVr2RhKkv5DfC9TJBQSU9uoQYvCzJ7Z45fpYpwIa8g","base64_sign":"MGUCME4pcfKJgBebtxJKBUrMQREWX0YVwssWdSxnT7AbG7S4Su8gjQdi/rlmfsp/28xLWQIxANxagG7zeO1ZwTnrryD5EC0p06WGqS+nU8YqRJg5VOxO1sgeDKHeIlTF4OUOb6fjig==","plaintext_message":"Hello World!!!!"}' | json_pp

Response: {
   "base64_sign" : "MGUCME4pcfKJgBebtxJKBUrMQREWX0YVwssWdSxnT7AbG7S4Su8gjQdi/rlmfsp/28xLWQIxANxagG7zeO1ZwTnrryD5EC0p06WGqS+nU8YqRJg5VOxO1sgeDKHeIlTF4OUOb6fjig==",
   "asymm_algo" : "eddsa",
   "base64_public_key" : "MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAEnQMUFAvChWOt+pf8f5miYpdpccyMFftjNDQouNjEnoA2TrUM67l72VGIenGUZu14zBqx+ZseqhQB1DrfyTmgWRcVr2RhKkv5DfC9TJBQSU9uoQYvCzJ7Z45fpYpwIa8g",
   "verified" : true,
   "plaintext_message" : "Hello World!!!!"
}

 */