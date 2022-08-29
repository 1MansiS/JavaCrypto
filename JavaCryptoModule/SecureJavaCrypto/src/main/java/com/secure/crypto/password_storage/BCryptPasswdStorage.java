package com.secure.crypto.password_storage;

import com.secure.crypto.utils.ReadPropertiesFile;
import org.bouncycastle.crypto.generators.BCrypt;
import java.util.Base64;

public class BCryptPasswdStorage {

    private ReadPropertiesFile readPropertiesFile = new ReadPropertiesFile();

    public String generatePasswdForStorage(String plainTextPasswd, String salt) {

        byte[] passwd = BCrypt.generate(
                plainTextPasswd.getBytes(),  // byte array of user supplied, low entropy password
                Base64.getDecoder().decode(salt), // 256 bit (16 byte), CSPRNG generated salt
                Integer.parseInt(readPropertiesFile.getValue("COST_FACTOR")) // cost factor, performs 2^14 iterations.
        );

        return Base64.getEncoder().encodeToString(passwd);
    }
}
