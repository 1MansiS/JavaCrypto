package com.secure.crypto.message_digest;

import com.secure.crypto.utils.ReadPropertiesFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MessageDigestAPI {

    private ReadPropertiesFile readPropertiesFile = new ReadPropertiesFile();

    /*
    This API generates the message digest on input data, using configured Message Digest algorithm.

    @param data : Base64 encode
    @return : Base64 encoded hash
     */
    public String generateMessageDigest(String data, String hashingAlgo) {


        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(data.getBytes()));

        byte[] buff = new byte[Integer.parseInt(readPropertiesFile.getValue("MD_DATA_READ_BUFF"))]; // read configured no of bytes at a time
        if(hashingAlgo == null) {hashingAlgo = readPropertiesFile.getValue("MD_ALGO");}

        int count = 0;

        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance(hashingAlgo); // Returns instance of configured algorithm implementation, from the first provider configured in java.security config file.
        } catch (NoSuchAlgorithmException e) {System.out.println("Algorithm " + hashingAlgo + " not supported by default provider. Error message " + e.getMessage()); System.exit(0);}

        try {
            while ((count = bis.read()) > 0) {
                digest.update(buff, 0, count); // repeated apply update method, till all content of this string is bundled up for digesting
            }
        } catch(IOException io) {System.out.println("Exception: while reading from data string. Error message "+ io.getMessage());}

        byte[] hash = digest.digest(); // once u have all content bundled up, than only apply digesting.

        return Base64.getEncoder().encodeToString(hash); // to return in a human readable format
    }
}
