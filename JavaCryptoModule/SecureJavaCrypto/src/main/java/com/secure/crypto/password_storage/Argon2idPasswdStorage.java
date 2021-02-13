package com.secure.crypto.password_storage;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.util.Base64;

public class Argon2idPasswdStorage {

    private int NO_OF_ITERATIONS = 10;
    private int ALLOCATED_MEMORY = 16777;
    private int PARALLELISM = 4;
   /*
   This API, returns Argonid version of plain text password ready for storage

   @param plainTextPasswd: Clear text password
   @param salt: Base64 encoded salt value

   @return : Base64 Encoded password to be stored
    */

    public String generatePasswdForStorage(String plainTextPasswd, String salt) {
        long start = System.currentTimeMillis();
        // Build Argon2 Parameters
        Argon2Parameters.Builder argon2Parameters = (new Argon2Parameters.Builder()).
                withVersion(Argon2Parameters.ARGON2_id) // For password storage recommended mode of operation to protect against both side-channel and timing attacks.
                .withIterations(NO_OF_ITERATIONS) // No of times memory array will be filled
                .withMemoryAsKB(ALLOCATED_MEMORY) // Amount of memory assigned, in KB
                .withParallelism(PARALLELISM) // # of Parallel processing units
                .withSecret(plainTextPasswd.getBytes()) //password
                .withSalt(Base64.getDecoder().decode(salt)); // 256 bits of CSPRNG unique salt

        Argon2BytesGenerator argon2passwordGenerator = new Argon2BytesGenerator();
        argon2passwordGenerator.init(argon2Parameters.build()); // Initializing Argon2 algorithm with configured parameters

        // Array to store computed hash
        byte[] passwdHash = new byte[32];


        argon2passwordGenerator.generateBytes(plainTextPasswd.getBytes(), passwdHash); // Finally, putting it all together to compute the password hash
        long end = System.currentTimeMillis();
        System.out.println("Argon2 took " + (end - start));
        return Base64.getEncoder().encodeToString(passwdHash);

    }

}
