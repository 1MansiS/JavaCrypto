package com.secure.crypto.key_generation;

import com.secure.crypto.utils.PropertiesFile;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SymmetricKeyGeneration {

    private PropertiesFile propertiesFile = new PropertiesFile();

    /*
    @param algo : Symmetric Key Algorithm to use

    @return : Base64 encoded Secret Key
     */

    public String generateSymmetricKey(String algo) {
        SecretKey aesKey = null;
        KeyGenerator keygen = null;
        try {
            keygen = KeyGenerator.getInstance(propertiesFile.getPropertyValue(algo)) ; // Specifying algorithm key will be used for
            keygen.init(Integer.parseInt(propertiesFile.getPropertyValue("AES_KEY_SIZE"))); // Specifying Key size to be used, Note: This would need JCE Unlimited Strength to be installed explicitly
            aesKey = keygen.generateKey();

        } catch (NoSuchAlgorithmException e) {System.out.println("Algorithm " + propertiesFile.getPropertyValue(algo) + " not supported by provider " +keygen.getProvider().toString() + ". Error message " + e.getMessage()); System.exit(0);}

        return Base64.getEncoder().encodeToString(aesKey.getEncoded());
    }
}
