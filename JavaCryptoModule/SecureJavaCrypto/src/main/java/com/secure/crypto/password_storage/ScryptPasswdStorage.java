package com.secure.crypto.password_storage;

import org.bouncycastle.crypto.generators.SCrypt;
import java.util.Base64;

public class ScryptPasswdStorage {

    private int BLOCK_SIZE = 16;
    private int PARALLELIZATION = 1;
    private int CPU_MEMORY_COST = 65536;
    private int OUTPUT_LENGTH = 32;

    public String generatePasswdForStorage(String plainTextPasswd, String salt) {
        byte[] passwd =  SCrypt.generate(
                plainTextPasswd.getBytes(),
                Base64.getDecoder().decode(salt),
                CPU_MEMORY_COST,
                BLOCK_SIZE ,
                PARALLELIZATION,
                OUTPUT_LENGTH);

        return Base64.getEncoder().encodeToString(passwd);

    }
}
