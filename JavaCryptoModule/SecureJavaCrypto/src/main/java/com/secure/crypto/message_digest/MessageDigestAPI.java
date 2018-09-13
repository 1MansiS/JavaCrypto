package com.secure.crypto.message_digest;

import com.secure.crypto.utils.PropertiesFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MessageDigestAPI {

    private PropertiesFile propertiesFile = new PropertiesFile();

    /*
    data : Base64 encode
    returns : Base64 encoded hash
     */
    public String generateMessageDigest(String data) {

        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(data)));
        byte[] buff = new byte[Integer.parseInt(propertiesFile.getPropertyValue("MD_DATA_READ_BUFF"))];

        int count = 0;

        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance(propertiesFile.getPropertyValue("MD_ALGO"));
        } catch (NoSuchAlgorithmException e) {System.out.println("Algorithm " + propertiesFile.getPropertyValue("MD_ALGO") + " not supported by default provider " ); System.exit(1);}

        try {
            while ((count = bis.read()) > 0) {
                digest.update(buff, 0, count); // repeated apply update method, till all content of this string is bundled up for digesting
            }
        } catch(IOException io) {System.out.println("Exception: while reading from data string "+ io.getMessage());}

        byte[] hash = digest.digest(); // once u have all content bundled up, than only apply digesting.

        return Base64.getEncoder().encodeToString(hash);
    }
}
