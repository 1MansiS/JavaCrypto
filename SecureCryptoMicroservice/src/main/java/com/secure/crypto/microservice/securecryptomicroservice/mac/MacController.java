package com.secure.crypto.microservice.securecryptomicroservice.mac;

import com.secure.crypto.key_generation.SymmetricKeyGeneration;
import com.secure.crypto.mac.MACComputationAPI;
import com.secure.crypto.microservice.securecryptomicroservice.utils.FilePersistance;
import java.util.Base64;

import org.springframework.web.bind.annotation.*;

@RestController
public class MacController {

    MACComputationAPI macComputationAPI = new MACComputationAPI();
    SymmetricKeyGeneration symmetricKeyGeneration = new SymmetricKeyGeneration();
    FilePersistance filePersistance = new FilePersistance();

    private String HMAC_KEY_FILENAME = "mac.key" ;
    /*
    Calculate MAC of input data, to be sent across to receiver for integrity and authenticity checks.

    @param data == data to be mac'ced
    @return Base64 encoded mac value
     */
    @PostMapping("mac_sender")
    public @ResponseBody String macSender(String data) {

        String hmacKey = symmetricKeyGeneration.generateSymmetricKey("HMAC_ALGORITHM");

        // SECURITY RISK, to persist HMAC keying material on disk. A KMS should be used Done here for sake of KISS (Keeping it simple and stupid)
        // Need to persist this Symmetric Key, for re-computing MacTag on receiver side.
        filePersistance.persistCipherMaterial(hmacKey, HMAC_KEY_FILENAME);


        return macComputationAPI.computeMac(hmacKey,data) + "\n";
    }

    /*
    Receiver template endpoint. This endpoint re-computes MAC of input data and checks for authenticity and integrity by checking with input mac.

    @param data == data to be mac'ced
    @param mac == base64 encoded mac value

    @return boolean, if mac is verified for integrity and authenticity
     */
    @PostMapping("mac_receiver")
    public @ResponseBody boolean macReceiver(String data,String mac) {

        // SECURITY RISK, to persist HMAC keying material on disk. A KMS should be used Done here for sake of KISS (Keeping it simple and stupid)
        String hmacKey = filePersistance.readCipherMaterial(HMAC_KEY_FILENAME);

        // Re-compute MacTag, using same Symmetric Key
        if (macComputationAPI.computeMac(hmacKey, data).equals(mac)){
            return true;
        } else {
            return false;
        }
    }
}
