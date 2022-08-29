package com.secure.crypto.password_storage;

import com.secure.crypto.utils.ReadPropertiesFile;
import org.bouncycastle.crypto.generators.SCrypt;
import java.util.Base64;

public class ScryptPasswdStorage {

    private ReadPropertiesFile readPropertiesFile = new ReadPropertiesFile();

    public String generatePasswdForStorage(String plainTextPasswd, String salt) {
        byte[] passwd =  SCrypt.generate(
                plainTextPasswd.getBytes(),
                Base64.getDecoder().decode(salt),
                Integer.parseInt(readPropertiesFile.getValue("CPU_MEMORY_COST")),
                Integer.parseInt(readPropertiesFile.getValue("BLOCK_SIZE")) ,
                Integer.parseInt(readPropertiesFile.getValue("PARALLELIZATION")),
                Integer.parseInt(readPropertiesFile.getValue("OUTPUT_LENGTH"))
                );

        return Base64.getEncoder().encodeToString(passwd);

    }
}
