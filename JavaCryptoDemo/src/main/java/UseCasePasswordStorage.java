import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.security.DrbgParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static java.security.DrbgParameters.Capability.PR_AND_RESEED;

public class JavaCryptoDemo {
    public static void main(String args[]) {

        String plainTextPasswd = args[0];

        // Step 1. Generate Salt
        SecureRandom drbgSecureRandom = null;
        try {
            drbgSecureRandom = SecureRandom.getInstance("DRBG", // Uses default configured DRBG mechanism which is usually SHA-256 Hash. Good idea to check in your java.security file.
                    DrbgParameters.instantiation(256, // Security strength, default is 128 as configured in java.security
                            PR_AND_RESEED, // prediction resistance, and re-seeding support.
                            // Prediction Resistance ==  compromise of the DRBG internal state has no effect on the security of future DRBG outputs.
                            // Reseeding == Periodic reseeding, to avoid too many output from a seed
                            "any_hardcoded_string".getBytes()));
        } catch (NoSuchAlgorithmException e) { System.out.println("DRBG algorithm for generating CSPRNG is not supported"); }
        byte[] salt = new byte[32];
        drbgSecureRandom.nextBytes(salt);

        /*
        curl 'http://localhost:8080/compute-salt' -s | json_pp
        */

        // Step 2. Compute Password hash for storage using Argon2 algorithm
        int NO_OF_ITERATIONS = 10;
        int ALLOCATED_MEMORY = 16777;
        int PARALLELISM = 4;

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
        String passwordHash = Base64.getEncoder().encodeToString(passwdHash);
        System.out.println("Argon2 computed Password Hash == " + passwordHash);

        /*
curl 'http://localhost:8080/compute-kdf-passwd' -X POST -H "Content-type: application/json" -s \
-d '{"base64-salt":"...",
"passwd":"mysupersecretpasswordtobestored!!!"}'| json_pp
         */
    }
}
