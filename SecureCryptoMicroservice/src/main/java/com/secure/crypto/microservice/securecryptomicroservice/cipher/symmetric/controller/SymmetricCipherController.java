package com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.controller;

import com.secure.crypto.cipher.symmetric.CipherAPI;
import com.secure.crypto.key_generation.SymmetricKeyGeneration;
import com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.entity.Cipher;
import com.secure.crypto.microservice.securecryptomicroservice.utils.FilePersistance;
import com.secure.crypto.secure_random.SecureRandomAPI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class SymmetricCipherController {

    private static int IV_SIZE = 96 ;
    private String ENC_KEY_FILE_NAME = "encryption.key";
    private String IV_FILE_NAME = "iv.txt";

    SymmetricKeyGeneration symmetricKeyGeneration = new SymmetricKeyGeneration();
    SecureRandomAPI secureRandomAPI = new SecureRandomAPI();
    CipherAPI cipherAPI = new CipherAPI();
    FilePersistance filePersistance = new FilePersistance();
    /*
    returns: Base64 encrypted value
     */

    @GetMapping("encrypt/{plainText}")
    public @ResponseBody String encrypt(@PathVariable String plainText) {

        Cipher cipher = new Cipher();

        cipher.setKey(symmetricKeyGeneration.generateSymmetricKey("")); // In real world situation, u should be using a Key Management Service.

        // Ideally Encryption Key, should be saved in a KMS system. However to simplify that process, currently persisting on filesystem
        filePersistance.persistCipherMaterial(cipher.getKey(), ENC_KEY_FILE_NAME);

        cipher.setInitializationVector(secureRandomAPI.generateCSPRNG(IV_SIZE));

        // IV should be persisted for use in decryption process. However to simplify that process, currently persisting on filesystem.
        filePersistance.persistCipherMaterial(cipher.getInitializationVector(), IV_FILE_NAME);

        cipher.setData(plainText);

        try {
            cipher.setAad(InetAddress.getLocalHost().getCanonicalHostName());
        } catch (UnknownHostException e) {System.out.println("Exception: While trying to get AAD data " + cipher.getAad());}

        return cipherAPI.encrypt(cipher.getKey(), cipher.getInitializationVector(), cipher.getAad(), cipher.getData());


    }

    /*
    returns: plaintext
     */
    @GetMapping("decrypt/{cipherText}")
    public @ResponseBody String decrypt(@PathVariable String cipherText) {
        Cipher cipher = new Cipher();
        cipher.setKey(filePersistance.readCipherMaterial(ENC_KEY_FILE_NAME));
        cipher.setInitializationVector(filePersistance.readCipherMaterial(IV_FILE_NAME));

        try {
            cipher.setAad(InetAddress.getLocalHost().getCanonicalHostName());
        } catch (UnknownHostException e) {System.out.println("Exception: While trying to get AAD data " + cipher.getAad());}

        return cipherAPI.decrypt(cipher.getKey(), cipher.getInitializationVector(), cipher.getAad(), cipherText);
    }


 }
