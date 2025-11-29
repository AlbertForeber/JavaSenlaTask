package com.senla.annotation_processor;

import com.senla.annotation.InjectTo;

import java.lang.reflect.Field;
import java.util.HashMap;


public class InjectProcessor {
    private static final HashMap<Class<?>, Object> DEPENDENCIES = new HashMap<>();

    public static void addDependency(Class<?> clazz, Object object) {
        DEPENDENCIES.put(clazz, object);
    }
    public static void injectDependencies(Object object) throws IllegalArgumentException {
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(InjectTo.class) != null) {
                Object dependency = DEPENDENCIES.get(field.getType());

                if (dependency != null) {
                    field.trySetAccessible();

                    try {
                        field.set(object, dependency);
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException("Ошибка изменения приватного поля '" + field.getName() + "'. Нет доступа");
                    }

                } else {
                    throw new IllegalArgumentException("Зависимости для поля типа '" + field.getType().getSimpleName() + "' не найдено");
                }
            }
        }
    }
}
