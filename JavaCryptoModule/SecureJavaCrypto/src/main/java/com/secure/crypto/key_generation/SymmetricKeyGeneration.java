package com.secure.crypto.key_generation;

import com.secure.crypto.utils.ReadPropertiesFile;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SymmetricKeyGeneration {

    private ReadPropertiesFile readPropertiesFile = new ReadPropertiesFile();
    /***
     * Returns a Base64 encoded Symmetric Key of provided algorithm and key size.
     * @param encryptionAlgo supported encrypted algorithm
     * @param symmetricKeySize required key size.
     * @param secRandom Provided configured instance of SecureRandom
     * @return base64 encoded version of generated symmetric key
     */
    public String generateSymmetricKey(String encryptionAlgo, int symmetricKeySize, SecureRandom secRandom) {

        SecretKey secretKey = null;
        KeyGenerator keyGenerator = null ;

        if(encryptionAlgo == null) {encryptionAlgo = readPropertiesFile.getValue("DEFAULT_ENC_ALGO");}
        else {encryptionAlgo = "ChaCha20";}

        // Preparing Key generation object
        try {
            keyGenerator = KeyGenerator.getInstance(encryptionAlgo);
        } catch (NoSuchAlgorithmException e) { System.out.println(encryptionAlgo + "is not supported"); }

        keyGenerator.init(symmetricKeySize , secRandom);

        // Key generation
        secretKey = keyGenerator.generateKey();

        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}