module SecureJavaCrypto {


    exports com.secure.crypto.secure_random;

    exports com.secure.crypto.cipher.symmetric;
    exports com.secure.crypto.key_generation;

    exports com.secure.crypto.message_digest;

    exports com.secure.crypto.mac;

    //requires bcprov.jdk16;
    requires org.bouncycastle.provider;
    exports com.secure.crypto.digital_signature;
}