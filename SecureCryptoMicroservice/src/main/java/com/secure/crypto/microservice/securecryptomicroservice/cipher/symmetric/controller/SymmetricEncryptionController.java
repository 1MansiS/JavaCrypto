package com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.controller;

import com.secure.crypto.cipher.symmetric.CipherAPI;
import com.secure.crypto.key_generation.SymmetricKeyGeneration;
import com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.entity.SymmetricEncryption;
import com.secure.crypto.secure_random.SecureRandomAPI;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;


@RestController
public class SymmetricEncryptionController {

    SymmetricKeyGeneration symmetricKeyGeneration = new SymmetricKeyGeneration();
    SecureRandomAPI secureRandomAPI = new SecureRandomAPI();
    CipherAPI cipherAPI = new CipherAPI();

    /**
     * This endpoint generates all the required encryption parameter depending on AES or ChaCha20 algorithm. If no algorithm is provided it defaults to AES.
     * Sample request: curl 'http://localhost:8080/generate-encryption-parameters/{enc_algo}'  -s | json_pp
     * @param enc_algo: Optional encryption Algorithm to be used. Defaults to AES
     * @return: JSON string with symmetric key and initialization vector as required by the algorithm
     * {
     *    "base64_iv" : "fpP9LW9idu4EDfWK/KbI+A==",
     *    "base64_symmetric_key" : "N+LweaNH3UXFq4UFyBjlH7w8R4R5/1vk+JTDhMHOvxU="
     * }
     */
    @RequestMapping(value={"/generate-encryption-parameters","/generate-encryption-parameters/{enc_algo}"})
    public SymmetricEncryption generateSymmetricEncryptionParameters(@PathVariable(required = false) String enc_algo) {
        SymmetricEncryption symmetricEncryption = new SymmetricEncryption();

        int keySize = 256; // Since Key size is same for AES or ChaCha algorithms

        symmetricEncryption.setBase64EncodedKey(
                symmetricKeyGeneration.generateSymmetricKey(
                        symmetricEncryption.getEncAlgo(),
                        keySize,
                        secureRandomAPI.drbgSecureRandom()
                ));

        int ivSize = 16; // Defaults to AES, which has key size = 16.
        
        if(enc_algo != null && enc_algo.toLowerCase().startsWith("chacha")){ivSize = 12;} // ChaCha20 needs IV == 12
        else if(enc_algo != null && enc_algo.toLowerCase().startsWith("AES")) {ivSize = 16; }
        else if(enc_algo == null) {ivSize = 16; }

        byte[] iv = new byte[ivSize];
        SecureRandom secureRandom = secureRandomAPI.drbgSecureRandom();
        secureRandom.nextBytes(iv);
        symmetricEncryption.setBase64EncodeIV(Base64.getEncoder().encodeToString(iv));

        // We can figure out the Enc Algo from the length of IV, so no need to setEncAlgo for later use
        return symmetricEncryption;
    }


    /***
     * /encrypt endpoint is called to do the actual encryption, taking in the key and iv generated thru above endpoints. It gives out the cipher text.
     * Sample request: curl 'http://localhost:8080/encrypt' -X POST -H "Content-Type: application/json" -d '{"symmetric_key":"...","IV":"...","plain_text":"Hello Crypto World!","aad":"localhost"}'
     * @param symmetricEncryption : Should pass symmetric_key, IV, plain_text, enc_algo and aad (only for AES) properties in json string
     * @return  : Return cipher text (base 64 encoded):
     * {
     *    "plain_text" : "Hello Crypto World!",
     *    "base64_cipher_text" : "OvnpZsO0gzfZ+yRCugFWLSgl5MZmj4VZNX8tf00jCViWk4o=",
     *    "symmetric_key" : "0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y=",
     *    "aad" : "localhost",
     *    "IV" : "qVsGLYhOnzBbDUIyTk595w==",
     *    "enc_algo" : "AES"
     * }
     */
    @PostMapping(value="/encrypt",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public SymmetricEncryption encrypt(@RequestBody SymmetricEncryption symmetricEncryption) {

        symmetricEncryption.setBase64EncodedEncryptedCipherText(
                cipherAPI.encrypt(
                    symmetricEncryption.getBase64EncodedKey(),
                    symmetricEncryption.getBase64EncodeIV(),
                    symmetricEncryption.getAad(),
                    symmetricEncryption.getPlainText()
                )
        );

        return symmetricEncryption;

    }

    /***
     * This endpoint is called to decrypt cipher text to retrieve corresponding plain text. It expects keying material used for encryption and cipher text.
     * Sample request: curl 'http://localhost:8080/decrypt' -X POST -H "Content-Type: application/json" -d '{"symmetric_key":"...","IV":"...","base64_cipher_text":"OvnpZsO0gzfZ+yRCugFWLSgl5MZmj4VZNX8tf00jCViWk4o=","aad":"localhost"}'
     * @param symmetricEncryption : Pass in symmetric_key, IV, base64_cipher_text, enc_algo and aad (only for AES encryption)
     * @return : Corresponding plain text:
     * {
     *    "plain_text" : "Hello Crypto World!",
     *    "enc_algo" : "AES",
     *    "IV" : "qVsGLYhOnzBbDUIyTk595w==",
     *    "base64_cipher_text" : "OvnpZsO0gzfZ+yRCugFWLSgl5MZmj4VZNX8tf00jCViWk4o=",
     *    "aad" : "localhost",
     *    "symmetric_key" : "0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y="
     * }
     */
    @PostMapping(value="/decrypt",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SymmetricEncryption decrypt(@RequestBody SymmetricEncryption symmetricEncryption) {

        symmetricEncryption.setPlainText(
                cipherAPI.decrypt(symmetricEncryption.getBase64EncodedKey(),
                        symmetricEncryption.getBase64EncodeIV(),
                        symmetricEncryption.getAad(),
                        symmetricEncryption.getBase64EncodedEncryptedCipherText()
                )
        );

        return symmetricEncryption;
    }
}
