package com.secure.crypto.mac;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MACComputationAPI {

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
