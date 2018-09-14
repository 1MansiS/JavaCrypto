package com.secure.crypto.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFile {

    //String cryptoConfigPath = "./cryptoConfig.properties";

    public String getPropertyValue(String property) {

        String cryptoConfigPath = "cryptoConfig.properties";

        Properties cryptoProps = new Properties();

        try {
            cryptoProps.load(PropertiesFile.class.getClassLoader().getResourceAsStream(cryptoConfigPath));
        } catch (IOException e) {System.out.println("Exception: can't find properties file " +  " Error message " + e.getMessage() ) ; System.exit(1);}

        return cryptoProps.getProperty(property);
    }
}
