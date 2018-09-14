package com.secure.crypto.microservice.securecryptomicroservice.message_digest;

import com.secure.crypto.message_digest.MessageDigestAPI;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageDigestController {

    private MessageDigestAPI messageDigestAPI = new MessageDigestAPI();

    /*
    This endpoint computes SHA-512 message digest for input data

    @param : Data for which message digest needs to be computed.
    @return: Base64 encoded, message digest.
     */
    @PostMapping("message_digest")
    public @ResponseBody String computeMessageDigest(String data) {
        return messageDigestAPI.generateMessageDigest(Base64.encodeBase64String(data.getBytes())) + "\n";
    }
}
