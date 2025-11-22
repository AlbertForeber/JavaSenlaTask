package task.utils;

import java.util.Properties;

public final class PropertyConverter {
    public static int getInt(Properties properties, String key, int defaultValue) {
        try {
            String value = properties.getProperty(key);
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        try {
            String value = properties.getProperty(key);
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
