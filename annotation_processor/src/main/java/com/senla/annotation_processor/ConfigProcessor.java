package com.senla.annotation_processor;

import com.senla.annotation.ConfigProperty;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public final class ConfigProcessor {

    private final static Map<Class<?>, Function<String, Object>> TYPE_PARSERS = Map.ofEntries(
            // Integer types
            Map.entry(Integer.class, Integer::valueOf),
            Map.entry(int.class, Integer::valueOf),

            // Double types
            Map.entry(Double.class, Double::valueOf),
            Map.entry(double.class, Double::valueOf),

            // Float types
            Map.entry(Float.class, Float::valueOf),
            Map.entry(float.class, Float::valueOf),

            // Long types
            Map.entry(Long.class, Long::valueOf),
            Map.entry(long.class, Long::valueOf),

            // Boolean types
            Map.entry(Boolean.class, Boolean::valueOf),
            Map.entry(boolean.class, Boolean::valueOf),

            // Short types
            Map.entry(Short.class, Short::valueOf),
            Map.entry(short.class, Short::valueOf),

            // Byte types
            Map.entry(Byte.class, Byte::valueOf),
            Map.entry(byte.class, Byte::valueOf),

            // String and Character types
            Map.entry(String.class, (String s) -> s),
            Map.entry(Character.class, (String s) -> s.charAt(0)),
            Map.entry(char.class, (String s) -> s.charAt(0))
    );

    private ConfigProcessor() { }

    public static void applyConfig(Object object) throws IllegalArgumentException {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            Properties properties;

            // Проверка, что нужная аннотация найдена
            if (annotation != null) {
                try {
                    properties = PropertyLoader.loadProperties(annotation.configFilePath());
                } catch (IOException e) {
                    throw new IllegalArgumentException("Конфиг с путем '" + annotation.configFilePath() + "' не найден");
                }

                // Используем значение по умолчанию для имени поля, если не указано в аннотации
                String propertyName = !annotation.propertyName().trim().isEmpty() ?
                        annotation.propertyName() : clazz.getSimpleName() + "." + field.getName();


                String value = properties.getProperty(propertyName);
                if (value == null) {
                    throw new IllegalArgumentException("Поля с именем '" + propertyName + "' не найдено в указанном конфиг-файле.");
                }


                if (field.getType() != annotation.type()) {
                    throw new IllegalArgumentException("Указанный тип '" + annotation.type().getSimpleName() + "' не совпадает с типом поля '" + field.getType().getSimpleName() + "'");
                }

                // Используем метод без исключений
                field.trySetAccessible();
                Object castedValue;

                try {
                    castedValue = castValue(annotation.type(), value);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Полученное значение поля '" + value + "' не удалось привести к типу поля '" + annotation.type().getSimpleName() + "'");
                }


                try {
                    field.set(object, castedValue);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Ошибка изменения приватного поля '" + field.getName() + "'. Нет доступа");
                }
            }
        }
    }


    private static Object castValue(Class<?> attributionType, String value) {
        return TYPE_PARSERS.get(attributionType).apply(value);
    }
}
