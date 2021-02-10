package com.secure.crypto.password_storage;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.util.Base64;

public class Argon2idPasswdStorage {

   /*
   This API, returns Argonid version of plain text password ready for storage

   @param plainTextPasswd: Clear text password
   @param salt: Base64 encoded salt value

   @return : Base64 Encoded password to be stored
    */

    public String generatePasswdForStorage(String plainTextPasswd, String salt) {
        // Build Argon2 Parameters
        Argon2Parameters.Builder argon2Parameters = (new Argon2Parameters.Builder()).
                withVersion(Argon2Parameters.ARGON2_id) // For password storage recommended mode of operation to protect against both side-channel and timing attacks.
                .withIterations(3) // No of times memory array will be filled
                .withMemoryAsKB(64) // Amount of memory assigned
                .withParallelism(4) // # of Parallel processing units
                .withSecret(plainTextPasswd.getBytes()) //password
                .withSalt(salt.getBytes()); // 64 bytes of CSPRNG unique salt

        Argon2BytesGenerator argon2passwordGenerator = new Argon2BytesGenerator();
        argon2passwordGenerator.init(argon2Parameters.build()); // Initializing Argon2 algorithm with configured parameters

        // Array to store computed hash
        byte[] passwdHash = new byte[32];


        argon2passwordGenerator.generateBytes(plainTextPasswd.getBytes(), passwdHash); // Finally, putting it all together to compute the password hash

        return Base64.getEncoder().encodeToString(passwdHash);

    }

}
