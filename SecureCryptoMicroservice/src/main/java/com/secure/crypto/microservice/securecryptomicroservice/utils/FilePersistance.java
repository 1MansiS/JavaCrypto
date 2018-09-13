package com.secure.crypto.microservice.securecryptomicroservice.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilePersistance {

    public void persistCipherMaterial(String cipherMaterial, String fileName) {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(cipherMaterial);
            writer.close();
        } catch (IOException e) {System.out.print("Exception: While trying to save cipher material in file " + fileName); System.exit(1); }
    }

    public String readCipherMaterial(String fileName) {
        String cipherMaterial = null;
        try {
            cipherMaterial = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {System.out.println("Exception: Couldn't read cipher material from file " + fileName); System.exit(1);}

        return cipherMaterial;
    }
}
