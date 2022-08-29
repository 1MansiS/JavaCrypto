package com.secure.crypto.microservice.securecryptomicroservice.mac.controller;

import com.secure.crypto.mac.MACComputationAPI;
import com.secure.crypto.microservice.securecryptomicroservice.mac.entity.Hmac;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class HmacController {

    private MACComputationAPI macComputationAPI = new MACComputationAPI();

    /***
     * Generate symmetric key, for configured HMAC algorithm and key size.
     * Choosing key size is not give to user, since key size should always be less than block size of underlying hashing algorithm.
     * This same key should be used on sender and receiver size, and safeguarded.
     * Sample Request: curl 'http://localhost:8080/compute-hmac-key/{hmac_enc_algo}' -X POST -H "Content-type: application/json" | json_pp
     * @param hmac_enc_algo : Optional Hmac algorithm to be used. Defaults to HmacSHA512, configured in JavaCrypto Module's DEFAULT_HMAC_ALGO property.
     * @return :
     * {
     *    "base64-symmetric-key" : "S20l/hZnaqoAvGx9CwdmJgeWOW7bsYJYPqebcECgMQs="
     * }
     *
     */
    @RequestMapping(value={"/compute-hmac-key","/compute-hmac-key/{hmac_enc_algo}"})
    public Hmac computeMacKey(@PathVariable(required = false)String hmac_enc_algo) {
        Hmac hmac =  new Hmac();

        hmac.setBase64SymmetricKey(
                macComputationAPI.computeMacKey(hmac_enc_algo)
        );

        return hmac;
    }

    /***
     * Computes MAC for passed plain text message, using computed symmetric key.
     * Sample Request: curl 'http://localhost:8080/compute-hmac' -X POST -H "Content-type: application/json" -d '{"message":"Hello MAC!!!","base64-symmetric-key":"S20l/hZnaqoAvGx9CwdmJgeWOW7bsYJYPqebcECgMQs="}' | json_pp
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
                        hmac.getMessage(),
                        hmac.getMac()
                )
        );

        return hmac;
    }
}