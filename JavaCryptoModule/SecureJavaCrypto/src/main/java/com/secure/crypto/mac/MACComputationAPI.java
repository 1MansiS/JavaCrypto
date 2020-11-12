package com.secure.crypto.mac;

import com.secure.crypto.utils.PropertiesFile;


import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MACComputationAPI {

    private PropertiesFile propertiesFile = new PropertiesFile();

    /*
    Deprecate
    @param key == Base64 Encoded SecretKey
    @param content == Text to be MACed

    @return Base64 Encoded MAC value
     */
    public String computeMac(String key, String content) {

        byte[] encodedKey = Base64.getDecoder().decode(key);
        Mac mac = null;

        try {
            mac = Mac.getInstance(propertiesFile.getPropertyValue("HMAC_ALGORITHM"));
        } catch (NoSuchAlgorithmException e) {System.out.println("Exception: Algorithm " + propertiesFile.getPropertyValue("HMAC_ALGORITHM") + " not supported by default provider. Error message " + e.getMessage()); System.exit(0);}

        try {
            mac.init(new SecretKeySpec(encodedKey, 0 , encodedKey.length, propertiesFile.getPropertyValue("HMAC_ALGORITHM")));
        } catch (InvalidKeyException e) {System.out.println("Exception: Key not valid. Error message " + e.getMessage()); System.exit(0);}

        mac.update(content.getBytes());

        return Base64.getEncoder().encodeToString(mac.doFinal());
    }

    /***
     * Provided plain text message, symmetric key and hmac algorithm, compute HMAC
     * @param base64SymmKey : Symmetric key generated
     * @param content : Plain text message to be hmaced
     * @param hmacAlgo : Algorithm to use to compute HMAC, note: symm key should be generated using same algorithm
     * @return : base64 encoded HMAC value
     */
    public String computeMac(String base64SymmKey, String content, String hmacAlgo) {
        SecretKey secretKey = new SecretKeySpec(
                        Base64.getDecoder().decode(base64SymmKey),
                    0,
                    Base64.getDecoder().decode(base64SymmKey).length,
                hmacAlgo);

        Mac mac = null;
        try {
            mac = Mac.getInstance(hmacAlgo);
        } catch (NoSuchAlgorithmException e) {System.out.println("Exception: Unable to initialize " + hmacAlgo);}

        try {
            mac.init(secretKey);
        } catch (InvalidKeyException e) {System.out.println("Exception: Unable to initialize with provided secrey key");}

        mac.update(content.getBytes());

        return Base64.getEncoder().encodeToString(mac.doFinal()) ;
    }
}
