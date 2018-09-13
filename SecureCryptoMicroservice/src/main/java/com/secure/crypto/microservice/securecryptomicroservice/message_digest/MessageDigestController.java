package com.secure.crypto.microservice.securecryptomicroservice.message_digest;

import com.secure.crypto.message_digest.MessageDigestAPI;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageDigestController {

    private MessageDigestAPI messageDigestAPI = new MessageDigestAPI();

    @GetMapping("message_digest/{data}")
    public @ResponseBody String computeMessageDigest(@PathVariable  String data) {
        return messageDigestAPI.generateMessageDigest(Base64.encodeBase64String(data.getBytes()));
    }
}
