package com.secure.crypto.key_generation;

import com.secure.crypto.utils.PropertiesFile;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.util.Base64;

public class AsymmetricKeyGeneration {
    PropertiesFile propertiesFile = new PropertiesFile();

    /*
    @return: String array, with Base64 encoded values of private key followed by public key
     */
    public String[] generateAsymmetricKey() {
	Security.addProvider( new BouncyCastleProvider());

        KeyPairGenerator keyPairGenerator  = null;

        try {
            keyPairGenerator = KeyPairGenerator.getInstance("Ed25519");
            //keyPairGenerator = KeyPairGenerator.getInstance(propertiesFile.getPropertyValue("ASYMMETRIC_ALGO"));
        } catch (NoSuchAlgorithmException e) {System.out.println("Exception: " + propertiesFile.getPropertyValue("ASYMMETRIC_ALGO")+ " not supported by default provider" + keyPairGenerator.getProvider().getName() + " Error message " + e.getMessage() ); System.exit(0);}


        //keyPairGenerator.initialize(Integer.parseInt(propertiesFile.getPropertyValue("ASYMMETRIC_KEY_SIZE")));

        KeyPair asymmKeyPair = keyPairGenerator.generateKeyPair();

        PrivateKey privateKey = asymmKeyPair.getPrivate();
        PublicKey publicKey = asymmKeyPair.getPublic();

        String[] retKeyMaterial = {Base64.getEncoder().encodeToString(privateKey.getEncoded()), Base64.getEncoder().encodeToString(publicKey.getEncoded())} ;
        return retKeyMaterial;
    }
}
