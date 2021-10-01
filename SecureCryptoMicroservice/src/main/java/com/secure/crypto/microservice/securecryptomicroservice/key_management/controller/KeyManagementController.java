package com.secure.crypto.microservice.securecryptomicroservice.key_management.controller;

import com.secure.crypto.key_generation.SymmetricKeyGeneration;
import com.secure.crypto.key_management.KeyManagementAPI;
import com.secure.crypto.microservice.securecryptomicroservice.key_management.entity.KeyStoreEntity;
import com.secure.crypto.secure_random.SecureRandomAPI;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@RestController
public class KeyManagementController {
    private KeyManagementAPI keyManagementAPI = new KeyManagementAPI();
    private SymmetricKeyGeneration symmetricKeyGeneration = new SymmetricKeyGeneration();
    private SecureRandomAPI secureRandomAPI = new SecureRandomAPI();

    /***
     * This endpoint stores an encryption password into keystore file.
     *
     * Sample Request: curl -X POST 'http://localhost:8080/store-secret-key' -H 'Content-Type: application/json' -d '{"key-store-password":"my-keystorepassword","entry-password":"my-entrypassword","alias":"encryption-key"}'  | json_pp
     *
     * @param key-store-password : Password used to encrypt the entire keystore
     * @param entry-password : Password to store this specific encryption key
     * @param alias: alias to represent this entry
     * @return : Returns a success or failure message
     * Sample Response:
     * {
     *    "key-store-password" : "my-keystorepassword",
     *    "entry-password" : "my-entrypassword",
     *    "Response" : "Successfully saved Secret Key with alias encryption-key",
     *    "alias" : "encryption-key"
     * }
     */
    @PostMapping(value = "store-secret-key",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public KeyStoreEntity addSecretKey(@RequestBody KeyStoreEntity keyStoreEntity) {
        byte[] secretKey =
                symmetricKeyGeneration.generateSymmetricKey(
                        "AES",
                        256,
                        secureRandomAPI.drbgSecureRandom()
                ).getBytes();

        boolean successFullySaved = keyManagementAPI.addSecretKey(keyStoreEntity.getKeyStorePassword(),
                                        keyStoreEntity.getKeyStorePassword(),
                                        keyStoreEntity.getAlias(),
                                        secretKey
                );


        if (successFullySaved) {
            keyStoreEntity.setResponse_message("Successfully saved Secret Key with alias " + keyStoreEntity.getAlias()) ;
        } else {
            keyStoreEntity.setResponse_message("Exception saving Secret Key with alias " + keyStoreEntity.getAlias()) ;
        }

        return keyStoreEntity;
    }
}