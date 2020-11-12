package com.secure.crypto.microservice.securecryptomicroservice.mac.controller;

import com.secure.crypto.key_generation.SymmetricKeyGeneration;
import com.secure.crypto.mac.MACComputationAPI;
import com.secure.crypto.microservice.securecryptomicroservice.mac.entity.Hmac;

import java.security.SecureRandom;

import com.secure.crypto.secure_random.SecureRandomAPI;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class HmacController {

    private SecureRandomAPI secureRandomAPI = new SecureRandomAPI();
    private SymmetricKeyGeneration symmetricKeyGeneration = new SymmetricKeyGeneration();
    private MACComputationAPI macComputationAPI = new MACComputationAPI();


    private String HMAC_ALGO = "HmacSHA512" ;
    private int HMAC_KEY_SIZE = 256;

    /***
     * Generate symmetric key, for configured HMAC algorithm and key size.
     * This control is not give to user, since key size should always be less than block size of underlying hashing algorithm.
     * This same key should be used on sender and receiver size, and safeguarded.
     * Sample Request: curl 'http://localhost:8080/compute-hmac-key' -X POST -H "Content-type: application/json" | json_pp
     * @param  : No parameters are required to be passed
     * @return :
     * {
     *    "base64-symmetric-key" : "S20l/hZnaqoAvGx9CwdmJgeWOW7bsYJYPqebcECgMQs="
     * }
     *
     */
    @PostMapping(value="compute-hmac-key",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
            )
    public Hmac computeMacKey() {
        Hmac hmac =  new Hmac();

        SecureRandom drbgSecureRandom = secureRandomAPI.drbgSecureRandom();

        hmac.setBase64SymmetricKey(
                symmetricKeyGeneration.generateSymmetricKey(
                        HMAC_ALGO,
                        HMAC_KEY_SIZE,
                        drbgSecureRandom
                )
        );

        return hmac;
    }
    /***
     * Computes MAC for passed plain text message, using computed symmetric key.
     * Sample Request: curl 'http://localhost:8080/compute-mac' -X POST -H "Content-type: application/json" -d '{"message":"Hello MAC!!!","base64-symmetric-key":"S20l/hZnaqoAvGx9CwdmJgeWOW7bsYJYPqebcECgMQs="}' | json_pp
     * @param hmac : Pass message and base64-symmetric-key parameters
     * @return:
     *
     * {
     *    "mac" : "aDJbcZOCylQzqqT+yNHDyMoghfDOGBlKl4px3DjXVew=",
     *    "base64-symmetric-key" : "S20l/hZnaqoAvGx9CwdmJgeWOW7bsYJYPqebcECgMQs=",
     *    "message" : "Hello MAC!!!"
     * }
     */
    @PostMapping(value="compute-hmac",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Hmac computeMAC(@RequestBody Hmac hmac) {

        hmac.setMac(
                macComputationAPI.computeMac(
                        hmac.getBase64SymmetricKey(),
                        hmac.getMessage()
                )
        );

        return hmac;
    }
}