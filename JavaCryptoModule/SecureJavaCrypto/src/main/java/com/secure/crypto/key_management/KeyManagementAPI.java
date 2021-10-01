package com.secure.crypto.key_management;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeyManagementAPI {
    private String KEY_STORE_FILE = "keystore.p12";

    public boolean addSecretKey(String keyStorePassword, String entryPassword, String alias, byte[] secretKey) {
        KeyStore keyStore = null;

        try {
            // This will give us PKCS12 keystore initialized object with encryption and hashing setup as shown in above diagram, unless changed
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType()) ;

            // null could be deceiving, but it just means empty keystore. Using a password which is used just for keystore and is different from each individual entries.
            keyStore.load(null, keyStorePassword.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            System.out.println("Exception: Problem creating KeyStore file" + e.getMessage());
            System.exit(0);
        }

        // Assuming secret key is computed outside this code, and passed here as a byte array. So some key transformation
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(
                                                    new SecretKeySpec(
                                                            secretKey,
                                                            0,
                                                            secretKey.length,
                                                            "AES")
                                                    );

        try {
            keyStore.setEntry(
                        alias, // Entry specific alias.
                        secretKeyEntry,
                        new KeyStore.PasswordProtection(entryPassword.toCharArray()) // Entry specific password

            );

        } catch (KeyStoreException e) {
            System.out.println("Exception: while storing secret key with alias " + alias + " with exception" + e.getMessage());
            System.exit(0);
        }

        try (FileOutputStream fos = new FileOutputStream(KEY_STORE_FILE)) {
            keyStore.store(fos, keyStorePassword.toCharArray()); // Using keystore password
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            System.out.println("Exception: while storing keystore file with secret key " + e.getMessage());
            System.exit(0);
        }

        boolean retVal = false;
        try {
            if(keyStore.isKeyEntry(alias)) {
                retVal = true;
            } else {
                retVal = false;
            }
        } catch (KeyStoreException e) {
            System.out.println("Exception: key with alias " + alias + " not found. Exception : " + e.getMessage());
        }

        return retVal;
    }
}