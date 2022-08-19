package com.secure.crypto.microservice.securecryptomicroservice.message_digest.controller;

import com.secure.crypto.message_digest.MessageDigestAPI;
import com.secure.crypto.microservice.securecryptomicroservice.message_digest.entity.Digest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageDigestController {

    private MessageDigestAPI messageDigestAPI = new MessageDigestAPI();

    /***
     * This endpoint computes SHA3-512 digest if input message.
     * Sample Request: curl 'http://localhost:8080/digest' -X POST -H "Content-type: application/json" -d '{"message":"Hello Hashing!!!"}' | json_pp
     *
     * @param digest : Should pass "message" parameter
     * @return : Returns base64 encoded hash string
     * Sample Response:
     * {
     *    "hash" : "Mb9x21/z6XCh3OiwzWSfkxnybuKPRe0FiSqxLDkNDGRPcRzcvEHUrSRF6iseByz/qVtgXc3qYe4U1gWZkM2B7A==",
     *    "message" : "Hello Hashing!!!"
     * }
     */
    @PostMapping(value = "digest",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
                )
    public Digest computeMessageDigest(@RequestBody Digest digest) {
        digest.setHash(
                messageDigestAPI.generateMessageDigest(digest.getMessage(), digest.getHashingAlgo())
        );

        return digest;
    }
}
