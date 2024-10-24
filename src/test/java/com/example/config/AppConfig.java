package com.example.config;

import java.io.IOException;
import java.util.Properties;

public class AppConfig {

    private static AppConfig instance;
    private Properties properties;

    private AppConfig() {
        properties = new Properties();
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getBaseUrl() {
        return properties.getProperty("baseUrl");
    }
}