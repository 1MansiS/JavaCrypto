package com.secure.crypto.utils;

import java.io.IOException;
import java.util.Properties;

public class ReadPropertiesFile {

    private String pathToPropertiesFile = "cryptoConfig.properties";
    public String getValue(String key) {
        Properties prop = loadPropertiesFile();

        return prop.getProperty(key);

    }

    private Properties loadPropertiesFile() {
        Properties prop = new Properties();

        try {
            prop.load(getClass().getClassLoader().getResourceAsStream(pathToPropertiesFile));

        } catch (IOException ioException) {
            System.out.println("Couldn't load properties file " + pathToPropertiesFile + " due to " + ioException);
        }

        return prop;
    }
}
