package com.secure.crypto.mac;

import com.secure.crypto.utils.PropertiesFile;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MACComputationAPI {

    private PropertiesFile propertiesFile = new PropertiesFile();

    /*
    @param key == Base64 Encoded SecretKey
    @param content == Text to be MACed

    @return Base64 Encoded MAC value
     */
    public String computeMac(String key, String content) {

        byte[] encodedKey = Base64.getDecoder().decode(key);
        Mac mac = null;

        try {
            mac = Mac.getInstance(propertiesFile.getPropertyValue("HMAC_ALGORITHM"));
        } catch (NoSuchAlgorithmException e) {System.out.println("Exception: Algorithm " + propertiesFile.getPropertyValue("HMAC_ALGORITHM") + " not supported by default provider"); System.exit(1);}

        try {
            mac.init(new SecretKeySpec(encodedKey, 0 , encodedKey.length, propertiesFile.getPropertyValue("HMAC_ALGORITHM")));
        } catch (InvalidKeyException e) {System.out.println("Exception: Key not valid"); System.exit(0);}

        mac.update(content.getBytes());

        return Base64.getEncoder().encodeToString(mac.doFinal());
    }
}
