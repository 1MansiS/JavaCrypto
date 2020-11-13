package com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.controller;

import com.secure.crypto.cipher.symmetric.AESCipherAPI;
import com.secure.crypto.cipher.symmetric.ChaChaCipherAPI;
import com.secure.crypto.key_generation.SymmetricKeyGeneration;
import com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.entity.SymmetricEncryption;
import com.secure.crypto.secure_random.SecureRandomAPI;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Base64;


@RestController
public class SymmetricEncryptionController {

    SymmetricKeyGeneration symmetricKeyGeneration = new SymmetricKeyGeneration();
    SecureRandomAPI secureRandomAPI = new SecureRandomAPI();
    AESCipherAPI aesCipherAPI = new AESCipherAPI();
    ChaChaCipherAPI chaChaCipherAPI = new ChaChaCipherAPI();

    /***
     * This endpoint expects encryption algorithhm and key size to generate symmetric key. Generated Symmetric Key should be safely stored. Ideally using a KMS.
     * Sample request : curl 'http://localhost:8080/generate-symmetric-key' -X POST -H "Content-Type: application/json" -d '{"key_size":"256","enc_algo":"AES"}'
     * @param symmetricEncryption : key_size (specified in bit size) and enc_algo properties should be passed in json request
     * @return : Outputs, generated symmetric key (base 64 encoded):
     * {
     *    "enc_algo" : "AES",
     *    "symmetric_key" : "0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y=",
     *    "key_size" : 256
     * }
     */
    @PostMapping(value="/generate-symmetric-key",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public SymmetricEncryption  generateKey(@RequestBody SymmetricEncryption symmetricEncryption) {

        symmetricEncryption.setBase64EncodedKey(
                symmetricKeyGeneration.generateSymmetricKey(
                    symmetricEncryption.getEncAlgo(),
                    symmetricEncryption.getKeySize(),
                    secureRandomAPI.drbgSecureRandom()
        ));

        return symmetricEncryption;
    }

    /***
     * This endpoint generates initialization vector of specified size. Generated IV  should be treated as any keying material and be safely stored. Ideally using a KMS.
     * Sample request: curl 'http://localhost:8080/generate-initialization-vector' -X POST -H "Content-Type: application/json" -d '{"iv_size":"16"}'
     * @param symmetricEncryption : iv_size (specified in byte size) property should be passed in json request
     * @return : Outputs, generated IV (base 64 encoded):
     * {
     *    "iv_size" : 16,
     *    "IV" : "qVsGLYhOnzBbDUIyTk595w=="
     * }
     */
    @PostMapping(value="/generate-initialization-vector",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SymmetricEncryption generateIV(@RequestBody SymmetricEncryption symmetricEncryption) {
        byte[] iv = new byte[symmetricEncryption.getIvSize()];
        SecureRandom secureRandom = secureRandomAPI.drbgSecureRandom();
        secureRandom.nextBytes(iv);
        symmetricEncryption.setBase64EncodeIV(Base64.getEncoder().encodeToString(iv));

        return symmetricEncryption;
    }

    /***
     * /encrypt endpoint is called to do the actual encryption, taking in the key and iv generated thru above endpoints. It gives out the cipher text.
     * Sample request: curl 'http://localhost:8080/encrypt' -X POST -H "Content-Type: application/json" -d '{"symmetric_key":"0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y=","IV":"qVsGLYhOnzBbDUIyTk595w==","plain_text":"Hello Crypto World!","aad":"localhost","enc_algo":"AES"}'
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

        if (symmetricEncryption.getEncAlgo().compareToIgnoreCase("AES") == 0) {
            symmetricEncryption.setBase64EncodedEncryptedCipherText(
                    aesCipherAPI.encrypt(
                            symmetricEncryption.getBase64EncodedKey(),
                            symmetricEncryption.getBase64EncodeIV(),
                            symmetricEncryption.getAad(),
                            symmetricEncryption.getPlainText()
                    )
            );
        } else { // Its ChaCha20-Poly1305
            symmetricEncryption.setBase64EncodedEncryptedCipherText(
                    chaChaCipherAPI.encrypt(symmetricEncryption.getBase64EncodedKey(),
                            symmetricEncryption.getBase64EncodeIV(),
                            symmetricEncryption.getPlainText()
                    )
            );
        }

        return  symmetricEncryption;
    }

    /***
     * This endpoint is called to decrypt cipher text to retrieve corresponding plain text. It expects keying material used for encryption and cipher text.
     * Sample request: curl 'http://localhost:8080/decrypt' -X POST -H "Content-Type: application/json" -d '{"symmetric_key":"0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y=","IV":"qVsGLYhOnzBbDUIyTk595w==","base64_cipher_text":"OvnpZsO0gzfZ+yRCugFWLSgl5MZmj4VZNX8tf00jCViWk4o=","enc_algo":"AES","aad":"localhost"}'
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
        if(symmetricEncryption.getEncAlgo().compareToIgnoreCase("AES") == 0) {
                symmetricEncryption.setPlainText(
                        aesCipherAPI.decrypt(symmetricEncryption.getBase64EncodedKey(),
                        symmetricEncryption.getBase64EncodeIV(),
                        symmetricEncryption.getAad(),
                        symmetricEncryption.getBase64EncodedEncryptedCipherText()
                        )
                );
        } else { // Its ChaCha20-Poly1305
            symmetricEncryption.setPlainText(
                    chaChaCipherAPI.decrypt(
                            symmetricEncryption.getBase64EncodedKey(),
                            symmetricEncryption.getBase64EncodeIV(),
                            symmetricEncryption.getBase64EncodedEncryptedCipherText()
                    )
            );
        }

        return symmetricEncryption;
    }

    /***
     * Step 1:
     *
     * curl 'http://localhost:8080/generate-symmetric-key' -X POST -H "Content-Type: application/json" -d '{"key_size":"256","enc_algo":"ChaCha20"}' | json_pp
     * {
     *    "symmetric_key" : "wPhxPnRWuVmCyS/Mvak+KHMb2Uan0BgdvTYGMYJbC+M=",
     *    "enc_algo" : "ChaCha20",
     *    "key_size" : 256
     * }
     *
     * Step 2:
     *
     * curl 'http://localhost:8080/generate-initialization-vector' -X POST -H "Content-Type: application/json" -d '{"iv_size":"12"}' | json_pp
     * {
     *    "iv_size" : 12,
     *    "IV" : "kLOPGk+d7j5ppaw0"
     * }
     *
     * Step 3:
     *
     * curl 'http://localhost:8080/encrypt' -X POST -H "Content-Type: application/json" -d '{"symmetric_key":"wPhxPnRWuVmCyS/Mvak+KHMb2Uan0BgdvTYGMYJbC+M=","IV":"kLOPGk+d7j5ppaw0","plain_text":"Hello Crypto World!","enc_algo":"ChaCha20"}' | json_pp
     * {
     *    "IV" : "kLOPGk+d7j5ppaw0",
     *    "enc_algo" : "ChaCha20",
     *    "base64_cipher_text" : "OtG5e36uvUfjk7jFEymNSVrvDCsWpPg3Be4gBmuSLjrjd/E=",
     *    "symmetric_key" : "wPhxPnRWuVmCyS/Mvak+KHMb2Uan0BgdvTYGMYJbC+M=",
     *    "plain_text" : "Hello Crypto World!"
     * }
     *
     * Step 4:
     *
     * curl 'http://localhost:8080/decrypt' -X POST -H "Content-Type: application/json" -d '{"symmetric_key":"wPhxPnRWuVmCyS/Mvak+KHMb2Uan0BgdvTYGMYJbC+M=","IV":"kLOPGk+d7j5ppaw0","base64_cipher_text":"OtG5e36uvUfjk7jFEymNSVrvDCsWpPg3Be4gBmuSLjrjd/E=","enc_algo":"ChaCha20"}' | json_pp
     * {
     *    "symmetric_key" : "wPhxPnRWuVmCyS/Mvak+KHMb2Uan0BgdvTYGMYJbC+M=",
     *    "plain_text" : "Hello Crypto World!",
     *    "IV" : "kLOPGk+d7j5ppaw0",
     *    "enc_algo" : "ChaCha20",
     *    "base64_cipher_text" : "OtG5e36uvUfjk7jFEymNSVrvDCsWpPg3Be4gBmuSLjrjd/E="
     * }
     */

}
