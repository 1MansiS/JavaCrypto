package com.secure.crypto.microservice.securecryptomicroservice.mac;

import com.secure.crypto.key_generation.SymmetricKeyGeneration;
import com.secure.crypto.mac.MACComputationAPI;
import com.secure.crypto.microservice.securecryptomicroservice.utils.FilePersistance;
import java.util.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MacController {

    MACComputationAPI macComputationAPI = new MACComputationAPI();
    SymmetricKeyGeneration symmetricKeyGeneration = new SymmetricKeyGeneration();
    FilePersistance filePersistance = new FilePersistance();

    private String HMAC_KEY_FILENAME = "mac.key" ;
    /*
    @param data == data to be mac'ced

    @return Base64 encoded mac value
     */
    @GetMapping("mac_sender/{data}")
    public @ResponseBody String macSender(@PathVariable  String data) {

        String hmacKey = symmetricKeyGeneration.generateSymmetricKey("HMAC_ALGORITHM");
        filePersistance.persistCipherMaterial(hmacKey, HMAC_KEY_FILENAME);


        return macComputationAPI.computeMac(hmacKey,data);
    }

    /*
    @param data == data to be mac'ced
    @param mac == base64 encoded mac value

    @return boolean, if mac is verified for integrity and authenticity
     */
    @GetMapping("mac_receiver/{data}/{mac}")
    public @ResponseBody boolean macReceiver(@PathVariable String data, @PathVariable String mac) {
        String hmacKey = filePersistance.readCipherMaterial(HMAC_KEY_FILENAME);

        //String computedMac = macComputationAPI.computeMac(hmacKey, data);

        if (macComputationAPI.computeMac(hmacKey, data).equals(mac)){
            return true;
        } else {
            return false;
        }
    }
}
