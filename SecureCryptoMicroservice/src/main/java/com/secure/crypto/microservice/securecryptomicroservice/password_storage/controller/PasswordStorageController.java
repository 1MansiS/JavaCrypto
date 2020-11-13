package com.secure.crypto.microservice.securecryptomicroservice.password_storage.controller;

import com.secure.crypto.microservice.securecryptomicroservice.password_storage.entity.KDFPasswordStorageEntity;
import com.secure.crypto.password_storage.KDFPasswdStorage;
import com.secure.crypto.secure_random.SecureRandomAPI;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
public class PasswordStorageController {

    SecureRandomAPI secureRandomAPI = new SecureRandomAPI();
    KDFPasswdStorage kdfPasswdStorage = new KDFPasswdStorage();


    /***
     * Compute salt to be used for compute password storage hash.
     * Sample Request: curl 'http://localhost:8080/compute-salt' -X POST -H "Content-type: application/json"  -d '{"salt-size":"20"}'| json_pp
     * @param kdfPasswordStorageEntity : "salt-size" parameter specified in bytes
     * @return:
     * {
     *    "base64-salt" : "Ihg4MIicY8hrWhiBw7Ogf6FEUZ4=",
     *    "salt-size" : "20"
     * }
     */
    @PostMapping(value="compute-salt",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE

                )
    public KDFPasswordStorageEntity computeSalt(@RequestBody KDFPasswordStorageEntity kdfPasswordStorageEntity) {
        SecureRandom drbgSecureRandom = secureRandomAPI.drbgSecureRandom();
        byte[] salt = new byte[Integer.parseInt(kdfPasswordStorageEntity.getSaltSize())];
        drbgSecureRandom.nextBytes(salt);

        kdfPasswordStorageEntity.setBase64Salt(Base64.getEncoder().encodeToString(salt));

        return kdfPasswordStorageEntity;
    }

    /***
     * Compute hash of plain text password using a kdf function. Never store plain text password in clear, always store it by compute it using a strong kdf.
     * Sample Request: curl 'http://localhost:8080/compute-kdf-passwd' -X POST -H "Content-type: application/json"  -d '{"base64-salt":"W+OXsj35qDPm9Xl4FdPDcz26pGE=","passwd":"mysupersecretpasswordtobestored!!!"}'| json_pp
     * @param kdfPasswordStorageEntity : "base64-salt" and "passwd"
     * @return :
     * {
     *    "base64-kdf-passwd-hash" : "Y241DH7LqmzUs9mpr0sgyPXNbdsXAISHNPUJUq8e9qnJ8YEVXN4qsQl8AZza94GxhrFED9Ntz2ATj9bRhn/iqmfTuE9FM42Z3er5F35Ud8jZdGUr12fb6GEUqRqwEhc9cSfMGsJ9lakAkXLWVQ6oUHL9lV7MDcgS/GBk3w4ZxOlP0OLsiR7r4qDB0fh4OTFR8J1Tw+usKVSXbuzqdWTLOo63On7CwfmhXW1fdf7A2cV5M6XvfVUquk/Sx1XbgVOBknvKcW5ugAIdj5Esnt8fSZhqRRtci600QpMAPPXogL6R4G/VOgMp2tabAQT7k7qB+gSlivmC4YAkxTMshFRokw==",
     *    "base64-salt" : "W+OXsj35qDPm9Xl4FdPDcz26pGE=",
     *    "passwd" : "mysupersecretpasswordtobestored!!!"
     * }
     */
    @PostMapping(value="compute-kdf-passwd",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE

            )
    public KDFPasswordStorageEntity computeKDFPassword(@RequestBody KDFPasswordStorageEntity kdfPasswordStorageEntity) {

        kdfPasswordStorageEntity.setBase64KDFPasswd(
            kdfPasswdStorage.generatePasswdForStorage(
                kdfPasswordStorageEntity.getPlainTextPassword(),
                kdfPasswordStorageEntity.getBase64Salt()
            )
        );

        return kdfPasswordStorageEntity;
    }
}
