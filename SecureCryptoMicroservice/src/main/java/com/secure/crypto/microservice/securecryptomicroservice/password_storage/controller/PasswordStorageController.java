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
     * Sample Request: curl 'http://localhost:8080/compute-kdf-passwd' -X POST -H "Content-type: application/json"  -d '{"base64-salt":"Ihg4MIicY8hrWhiBw7Ogf6FEUZ4=","passwd":"mysupersecretpasswordtobestored!!!","kdf-algo":"pbkdf2"}'| json_pp
     * @param kdfPasswordStorageEntity : "base64-salt", "kdf-algo" and "passwd"
     * @return :
     * {
     *    "base64-kdf-passwd-hash" : "FdpfiqCAEhSZiX5u27WUV7Y0iI9Qw2huCBSNDRAGEFaZ84FmFSiU2Ws4wG9O5fBOy5bsdL7XXNhHCZvWcdXgsNjvwoKFc2muh2r0SFpm3/MbnZUrI63gsKXlcrbvpzdvArZ9DzRUz31TjyK0fKs2HcVjQ3BA4lD+4iY9HJZYDMfu/D1YMpe7MEpYhCnTfOb8FVfUsOyje0N4+zGm547XfHXIzt/JrCYgbqn5Imw7JaVmS9i9jUflgxBsc+lv2wZmbxQoJ9md/dvk4xD0P6hpT0vSKpK9uj6ZJ5sxPpOkZvpKmskSnpNamcWjw2IrbTAGi3buoDBqbPeyPuN3Spkrcw==",
     *    "kdf-algo" : "pbkdf2",
     *    "passwd" : "mysupersecretpasswordtobestored!!!",
     *    "base64-salt" : "Ihg4MIicY8hrWhiBw7Ogf6FEUZ4="
     * }
     */
    @PostMapping(value="compute-kdf-passwd",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE

            )
    public KDFPasswordStorageEntity computeKDFPassword(@RequestBody KDFPasswordStorageEntity kdfPasswordStorageEntity) {

        if(kdfPasswordStorageEntity.getPasswdHashingAlgo().compareToIgnoreCase("pbkdf2") == 0) {
            kdfPasswordStorageEntity.setBase64KDFPasswd(
                    kdfPasswdStorage.generatePasswdForStorage(
                            kdfPasswordStorageEntity.getPlainTextPassword(),
                            kdfPasswordStorageEntity.getBase64Salt()
                    )
            );
        } else { // Only other supportd algorithm is Argon2
            // ToDo
        }
        return kdfPasswordStorageEntity;
    }
}
