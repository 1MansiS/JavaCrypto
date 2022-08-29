package com.secure.crypto.microservice.securecryptomicroservice.digital_signature.controller;

import com.secure.crypto.digital_signature.ECDigitalSignatureAPI;
import com.secure.crypto.digital_signature.EdDigitalSignatureAPI;
import com.secure.crypto.key_generation.AsymmetricKeyGeneration;
import com.secure.crypto.microservice.securecryptomicroservice.digital_signature.entity.DigitalSignature;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.util.Base64;

@RestController
public class DigitalSignatureController {

    AsymmetricKeyGeneration asymmetricKeyGeneration = new AsymmetricKeyGeneration();
    EdDigitalSignatureAPI edDigitalSignatureAPI = new EdDigitalSignatureAPI();
    ECDigitalSignatureAPI ecDigitalSignatureAPI = new ECDigitalSignatureAPI();

    /***
     * Generated Asymmetric Keys. Specify the  digital signature  algorithm to be  used. Only accepted values are ed-curve (edward curves)  or eddsa(NIST curves)
     * Sample Request: curl -X POST 'http://localhost:8080/generate-digital-signature-parameters/{digital_signature_algo}' | json_pp
     * @param digital_signature_algo  :  Expects asymm_algo with only expects values of ed-curve or ecdsa
     * @return : base64 encoded public and private keys
     * {
     *    "digital_signature_algo" : "ed-curve",
     *    "base64_private_key" : "MC4CAQAwBQYDK2VwBCIEIOcF/tgt18Cv1F3i3Jw7SJL3RgbjeO9NxrfSOxVBIJ1B",
     *    "base64_public_key" : "MCowBQYDK2VwAyEADzMUhYWEdBCl6XUlc5/PT7BuvFjFayaaCp2ktatJRxc="
     * }
     */
    @RequestMapping(value={"/generate-digital-signature-parameters","/generate-digital-signature-parameters/{digital_signature_algo}"})
    public DigitalSignature generateDigitalSignatureParameters(@PathVariable(required = false)String digital_signature_algo) {
        DigitalSignature digitalSignature = new DigitalSignature();

        KeyPair keyPair = null;

        if (digital_signature_algo != null && digital_signature_algo.toLowerCase().contains("ed")) {keyPair = asymmetricKeyGeneration.generateEdAsymmetricKey();} // Edward Curves
        else if(digital_signature_algo != null && digital_signature_algo.toLowerCase().startsWith("ecdsa")){keyPair = asymmetricKeyGeneration.generateECAsymmetricKey();} // EDDSA NIST curves
        else if(digital_signature_algo == null) {keyPair = asymmetricKeyGeneration.generateEdAsymmetricKey(); } // Defaults to Ed Curve

        digitalSignature.setBase64EncodedPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        digitalSignature.setBase64EncodedPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));

        return digitalSignature;
    }

    /***
     * This endpoint, generated the digital signature for the message passed
     * Sample Request: curl -X POST 'http://localhost:8080/sign' -H  "Content-Type: application/json" \
     * -d '{"digital_signature_algo":"ed-curve","base64_private_key":"MC4CAQAwBQYDK2VwBCIEIEkeQ5bKh7NQt5TxRhIEaSkpB7XY4KFZ8M9zsG0heVPS","plaintext_message":"Hello World!"}' | json_pp
     * @param digitalSignature: Pass the asymm_algo, base64_private_key and plaintext_message
     * @return : Base 64 signature value:
     *{
     *    "base64_private_key" : "MC4CAQAwBQYDK2VwBCIEIAWrd/47VIbIFtbOE34Kwsj8Is1FsLBSXDpUMNpAZ/H1",
     *    "base64_sign" : "9dIBdU4Gjjb0BDtfmWIF1jl3ODbrAdaCjUlTfO3/JGxQu4VUW6LBiZrTTaeLMZhRdnQ+uw07uogIZey1iGlaAw==",
     *    "digital_signature_algo" : "ed-curve",
     *    "plaintext_message" : "Hello World!"
     * }
     */
    @PostMapping(value="/sign",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
        )
    public DigitalSignature sign(@RequestBody DigitalSignature digitalSignature) {

        if(digitalSignature.getAsymm_algo() != null && digitalSignature.getAsymm_algo().toLowerCase().contains("ed")) { // Use Edward Curves
            digitalSignature.setBase64Signature(
                    edDigitalSignatureAPI.sign(
                            digitalSignature.getPlaintext_message(),
                            digitalSignature.getBase64EncodedPrivateKey())
            );
        } else if(digitalSignature.getAsymm_algo() != null && digitalSignature.getAsymm_algo().toLowerCase().startsWith("ecdsa")){
            digitalSignature.setBase64Signature(
                    ecDigitalSignatureAPI.sign(
                            digitalSignature.getPlaintext_message(),
                            digitalSignature.getBase64EncodedPrivateKey()
                    )
            );
        } else if(digitalSignature.getAsymm_algo() == null) { // Defaults to Ed Curves
            digitalSignature.setAsymm_algo("ed-curve");
            digitalSignature.setBase64Signature(
                    edDigitalSignatureAPI.sign(
                            digitalSignature.getPlaintext_message(),
                            digitalSignature.getBase64EncodedPrivateKey())
            );
        }

        return digitalSignature;
    }

    /***
     * This endpoint, verifies the digital signature passed
     * Sample Request: curl -X POST 'http://localhost:8080/verify' -H  "Content-Type: application/json" \
     * -d '{"plaintext_message":"Hello World!","base64_public_key":"MCowBQYDK2VwAyEAJF7iwf5nmWq+5znvKj1+F4ILsKRK6QUYmEIocUNFLFc=","base64_sign":"9dIBdU4Gjjb0BDtfmWIF1jl3ODbrAdaCjUlTfO3/JGxQu4VUW6LBiZrTTaeLMZhRdnQ+uw07uogIZey1iGlaAw=="}' | json_pp
     * @param digitalSignature: Expects: asymm_algo, base64_public_key, plaintext_message and base64_sign
     * @return: If the signature is verified. Look for the value of verified  key in response json string.
     * {
     *    "base64_public_key" : "MCowBQYDK2VwAyEAq6ATMBErxIChQkN65+kWXr5kTXltNSkU2K7kkK/OEdU=",
     *    "base64_sign" : "N6JMKDPJZTfmlwVga2/vJrLrnIWHmcAmIS7hUUPo8qeST3R0b5LUosYBzknbRcdj3km0zC0P195VHkd0dSJQAA==",
     *    "digital_signature_algo" : "ed-curve",
     *    "plaintext_message" : "Hello World!",
     *    "verified" : true
     * }
     */
    @PostMapping(value="/verify",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DigitalSignature verify(@RequestBody DigitalSignature digitalSignature) {

        if(digitalSignature.getAsymm_algo() != null && digitalSignature.getAsymm_algo().toLowerCase().contains("ed")){ // Use Edward Curves
            digitalSignature.setVerified(
                    edDigitalSignatureAPI.verify(
                            digitalSignature.getPlaintext_message(),
                            digitalSignature.getBase64EncodedPublicKey(),
                            digitalSignature.getBase64Signature()
                    )
            );
        } else if(digitalSignature.getAsymm_algo() != null && digitalSignature.getAsymm_algo().toLowerCase().startsWith("ecdsa")){
            digitalSignature.setVerified(
                    ecDigitalSignatureAPI.verify(digitalSignature.getPlaintext_message(),
                            digitalSignature.getBase64EncodedPublicKey(),
                            digitalSignature.getBase64Signature()
                    )
            );
        } else if(digitalSignature.getAsymm_algo() == null) { // Defaults to Ed Curves
            digitalSignature.setAsymm_algo("ed-curve");
            digitalSignature.setVerified(
                    edDigitalSignatureAPI.verify(
                            digitalSignature.getPlaintext_message(),
                            digitalSignature.getBase64EncodedPublicKey(),
                            digitalSignature.getBase64Signature()
                    )
            );
        }

        return digitalSignature;
    }
}