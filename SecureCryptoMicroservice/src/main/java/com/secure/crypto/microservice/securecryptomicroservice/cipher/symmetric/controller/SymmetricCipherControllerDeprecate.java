package com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.controller;

import com.secure.crypto.cipher.symmetric.AESCipherAPI;

import com.secure.crypto.key_generation.SymmetricKeyGeneration;
import com.secure.crypto.microservice.securecryptomicroservice.cipher.symmetric.entity.CipherDeprecate;
import com.secure.crypto.microservice.securecryptomicroservice.utils.FilePersistance;
import com.secure.crypto.secure_random.SecureRandomAPI;

import org.springframework.web.bind.annotation.*;


import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class SymmetricCipherControllerDeprecate {

    private int IV_SIZE = 96 ;

    private String ENC_KEY_FILE_NAME = "encryption.key";
    private String IV_FILE_NAME = "iv.txt";

    SymmetricKeyGeneration symmetricKeyGeneration = new SymmetricKeyGeneration();
    SecureRandomAPI secureRandomAPI = new SecureRandomAPI();
    AESCipherAPI cipherAPI = new AESCipherAPI();
    FilePersistance filePersistance = new FilePersistance();

    /*
    Performs AES/GCM/PKCS5 encryption.

    @param: plain_text == Clear text data to be encrypted.
    @return: Base64 encrypted value
     */

    // For Real World service endpoint, can consider plain_text to be Base64 encoded.
    @PostMapping("encrypt-old")
    public @ResponseBody String encrypt(String plain_text) {


        CipherDeprecate cipherDeprecate = new CipherDeprecate();

        cipherDeprecate.setKey(symmetricKeyGeneration.generateSymmetricKey("ENC_ALGO")); // In real world situation, u should be using a Key Management Service.

        // SECURITY RISK, to persist cryptographic keying material on disk. A KMS should be used. Done here for sake of KISS (Keeping it simple and stupid)
        filePersistance.persistCipherMaterial(cipherDeprecate.getKey(), ENC_KEY_FILE_NAME);

        cipherDeprecate.setInitializationVector(secureRandomAPI.generateCSPRNG(IV_SIZE));

        // SECURITY RISK, to persist cryptographic keying material on disk. Done here for sake of KISS (Keeping it simple and stupid)
        // IV should be persisted for use in decryption process.
        // Use different key+IV pair for encrypting/decrypting different data. Make sure not to repeat Key + IV pair, for encrypting more than one plaintext.
        filePersistance.persistCipherMaterial(cipherDeprecate.getInitializationVector(), IV_FILE_NAME);

        cipherDeprecate.setData(plain_text);

        try {
            // Any random data can be used as tag. One of common example could be domain name... Should be same on decryption side as well.
            cipherDeprecate.setAad(InetAddress.getLocalHost().getCanonicalHostName());
        } catch (UnknownHostException e) {System.out.println("Exception: While trying to get AAD data " + cipherDeprecate.getAad() + " Error message " + e.getMessage());}

        return cipherAPI.encrypt(cipherDeprecate.getKey(), cipherDeprecate.getInitializationVector(), cipherDeprecate.getAad(), cipherDeprecate.getData()) + "\n";

    }

    /*
    This performs decryption process.
    @param: cipher_text: Cipher Text, of plain text generated from above endpoint.
    @return: clear text version of plaintext, or given cipher_text
     */
    @PostMapping("decrypt-old")
    public @ResponseBody String decrypt(String cipher_text) {

        CipherDeprecate cipherDeprecate = new CipherDeprecate();

        cipherDeprecate.setKey(filePersistance.readCipherMaterial(ENC_KEY_FILE_NAME));
        cipherDeprecate.setInitializationVector(filePersistance.readCipherMaterial(IV_FILE_NAME));

        try {
            cipherDeprecate.setAad(InetAddress.getLocalHost().getCanonicalHostName()); // Needn't be super secret.
        } catch (UnknownHostException e) {System.out.println("Exception: While trying to get AAD data " + cipherDeprecate.getAad());}

        return cipherAPI.decrypt(cipherDeprecate.getKey(), cipherDeprecate.getInitializationVector(), cipherDeprecate.getAad(), cipher_text) + "\n";
    }


 }
