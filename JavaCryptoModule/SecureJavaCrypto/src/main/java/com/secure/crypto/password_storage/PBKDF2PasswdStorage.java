package com.secure.crypto.password_storage;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PBKDF2PasswdStorage {

    private int ITERATION_COUNT = 150000;
    private int DERIVED_KEY_LENGTH = 32; // specify in bytes

    private String PBKDF_ALGO = "PBKDF2WithHmacSHA512";

    /*
    This API, returns PBKDF2 version of plain text password ready for storage

    @param plainTextPasswd: Clear text password
    @param salt: Base64 encoded salt value

    @return : Base64 Encoded password to be stored
     */
    public String generatePasswdForStorage(String plainTextPasswd, String salt) {
        // Strings are immutatable, so there is no way to change/nullify/modify its content after use. So always, collect and store security sensitive information in a char array instead.
        char[] charEnteredPassword = plainTextPasswd.toCharArray() ;

        PBEKeySpec keySpec = null ;

        try {
            keySpec = new PBEKeySpec(charEnteredPassword,
                                        Base64.getDecoder().decode(salt),
                                        ITERATION_COUNT,
                                        DERIVED_KEY_LENGTH * 8);
        } catch(NullPointerException|IllegalArgumentException e) {System.out.println("One of the argument is illegal. Salt " + salt + " may be of 0 length, iteration count " + ITERATION_COUNT + " is not positive or derived key length " + DERIVED_KEY_LENGTH + " is not positive." ); System.out.println("Error :" + e.getMessage()); System.exit(0);}

        SecretKeyFactory pbkdfKeyFactory = null ;

        try {
            pbkdfKeyFactory = SecretKeyFactory.getInstance(PBKDF_ALGO) ;
        } catch (NullPointerException|NoSuchAlgorithmException e) {System.out.println("Specified algorithm " + PBKDF_ALGO + "is not available by the provider " + pbkdfKeyFactory.getProvider().getName());System.out.println("Error : " + e.getMessage()); System.exit(0);}

        byte[] pbkdfHashedArray = null ;

        try {
            pbkdfHashedArray = pbkdfKeyFactory.generateSecret(keySpec).getEncoded() ;
        } catch (InvalidKeySpecException e) {System.out.println("Specified key specification is inappropriate"); System.out.println("Error : " + e.getMessage()); System.exit(0);}

        return Base64.getEncoder().encodeToString(pbkdfHashedArray);
    }
}
