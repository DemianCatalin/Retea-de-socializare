package com.example.reteadesocializare.config;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

public class Config {
    public static URL CONFIG_URL= Config.class.getClassLoader()
            .getResource("config.properties");


    public static Properties getProperties() {
        Properties properties=new Properties();
        try {
            String CONFIG_LOCATION = CONFIG_URL.getFile();
            properties.load(new FileReader(CONFIG_LOCATION));
            Objects.requireNonNull(properties);
            return properties;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot load config properties");
        }
    }
}
