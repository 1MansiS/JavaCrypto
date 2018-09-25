package com.secure.crypto.microservice.securecryptomicroservice.password_storage;

import com.secure.crypto.microservice.securecryptomicroservice.utils.FilePersistance;
import com.secure.crypto.password_storage.EncryptedPasswdStorage;
import com.secure.crypto.secure_random.SecureRandomAPI;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordStorageController {

    private int SALT_SIZE = 20 ;
    private String STRETCHED_PASSWD_FILENAME = "stretched_passwd.txt";
    private String STRETCHED_PASSWD_SALT_FILENAME = "stretched_salt.txt";

    SecureRandomAPI secureRandomAPI = new SecureRandomAPI();
    EncryptedPasswdStorage encryptedPasswdStorage = new EncryptedPasswdStorage();
    FilePersistance filePersistance = new FilePersistance();

    /*
    This endpoint, calculates salt and corresponding password to be stored, as secured means of storing password.
    @param plain_text_password: Plain Text password used for storage

    @return: Base64 Encoded pdkdbf2 computed password.
     */
    @PostMapping("calc_passwd_storage")
    public @ResponseBody String calculatePasswdForStorage(String plain_text_password) {

        // Each user has a salt and corresponding pbkdf2 password persisted in secure storage for future authentication.
        String salt = secureRandomAPI.generateCSPRNG(SALT_SIZE);
        String stretchedPassword = encryptedPasswdStorage.generatePasswdForStorage(plain_text_password, salt);

        // SECURITY RISK, to persist cryptographic keying material on disk. Done here for sake of KISS (Keeping it simple and stupid)
        // This is a mock of storing password in a secure storage for e.g. KMS or a database.
        filePersistance.persistCipherMaterial(salt, STRETCHED_PASSWD_SALT_FILENAME);
        filePersistance.persistCipherMaterial(stretchedPassword, STRETCHED_PASSWD_FILENAME);

        return stretchedPassword;
    }

    /*
    This method shows how to authenticate a user, whose password is stored as pbkdf2, alongwith corresponding salt.
     */
    @PostMapping("authenticate")
    public @ResponseBody boolean authenticate(String plain_text_password) {

        String salt = filePersistance.readCipherMaterial(STRETCHED_PASSWD_SALT_FILENAME);

        // Re-compute PBKDF2 password of user entered password with pdkdf2 stored password of same user. If they match, user is authenticated
        if (filePersistance.readCipherMaterial(STRETCHED_PASSWD_FILENAME).equals(encryptedPasswdStorage.generatePasswdForStorage(plain_text_password,salt))) {
            return true;
        } else {
            return false;
        }
    }
}
