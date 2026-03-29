package com.senla.annotation_processor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertyLoader {

    public static Properties loadProperties(String path) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        }

        return properties;
    }
}
