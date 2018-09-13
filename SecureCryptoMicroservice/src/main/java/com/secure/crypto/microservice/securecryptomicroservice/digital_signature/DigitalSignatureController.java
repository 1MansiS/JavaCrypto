package com.secure.crypto.microservice.securecryptomicroservice.digital_signature;

import com.secure.crypto.digital_signature.DigitalSignatureAPI;
import com.secure.crypto.key_generation.AsymmetricKeyGeneration;
import com.secure.crypto.microservice.securecryptomicroservice.utils.FilePersistance;
import org.springframework.web.bind.annotation.*;

@RestController
public class DigitalSignatureController {

    private String PUBLIC_KEY_FILE = "public.key";
    private String PRIVATE_KEY_FILE = "private.key";


    private AsymmetricKeyGeneration asymmetricKeyGeneration = new AsymmetricKeyGeneration();
    private DigitalSignatureAPI digitalSignatureAPI = new DigitalSignatureAPI();
    private FilePersistance filePersistance = new FilePersistance();


    /*
    @param data == Plain text Data to be signed
    @return == Base64 encoded digital signature
     */
    @GetMapping("digital_signature_sign/{data}")
    public @ResponseBody String sign(@PathVariable String data) {
        String[] keyPairs = asymmetricKeyGeneration.generateAsymmetricKey();

        // KMS should be used to manage keys. For sake of this demo, using file storage for persistance.
        filePersistance.persistCipherMaterial(keyPairs[0],PRIVATE_KEY_FILE);
        filePersistance.persistCipherMaterial(keyPairs[1],PUBLIC_KEY_FILE);

        return digitalSignatureAPI.sign(data,filePersistance.readCipherMaterial(PRIVATE_KEY_FILE));
    }

    /*
    @param data == Plain text Data to be signed
    @param sign == Base64 encoded digital signature
    @return == true/false is digital signature verified for Integrity, authenticity and non-repudiation
     */
    @PostMapping("digital_signature_verify")
    public @ResponseBody boolean verify(String data,String sign) {
        return digitalSignatureAPI.verify(data,filePersistance.readCipherMaterial(PUBLIC_KEY_FILE),sign);
    }
}