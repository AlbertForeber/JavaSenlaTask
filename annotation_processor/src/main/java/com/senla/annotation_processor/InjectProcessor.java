package com.senla.annotation_processor;

import com.senla.annotation.InjectTo;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class InjectProcessor {
    private static final HashMap<Class<?>, Object> DEPENDENCIES = new HashMap<>();

    public static void addDependency(Class<?> clazz, Object object) {
        DEPENDENCIES.put(clazz, object);
    }

    public static void injectDependencies(Object object) throws IllegalArgumentException {
        Class<?> clazz = object.getClass();


        for (Field field : clazz.getDeclaredFields()) {
            InjectTo annotation = field.getAnnotation(InjectTo.class);
            Class<?> fieldType = field.getType();


            if (annotation != null) {


                Class<?> useImplementation = annotation.useImplementation();
                boolean configurable = annotation.configurable();


                String implementationErrorAddition = "";
                Object dependency = DEPENDENCIES.get(fieldType);

                field.trySetAccessible();

                if (dependency == null) {
                    // Создаем новый объект

                    try {
                        Constructor<?> constructor;

                        if (useImplementation != Object.class) {
                            if (!checkFieldType(useImplementation, fieldType))
                                throw new IllegalArgumentException("Указанная реализация '" + useImplementation.getSimpleName() +
                                        "' не наследует заданный тип поля '" + fieldType.getSimpleName() + "'");
                            constructor = useImplementation.getConstructor();
                            implementationErrorAddition = ". При указанной реализации '" + useImplementation.getSimpleName() + "'";
                        } else constructor = fieldType.getConstructor();


                        constructor.trySetAccessible();
                        dependency = constructor.newInstance();
                        addDependency(fieldType, dependency);

                    } catch (NoSuchMethodException e) {
                        throw new IllegalArgumentException("Для объекта типа '" + fieldType.getSimpleName() +
                                "' нет конструктора без параметров" + implementationErrorAddition);
                    } catch (InvocationTargetException e) {
                        throw new IllegalArgumentException("Ошибка создания объекта типа '" + fieldType.getSimpleName() + "'" +
                                implementationErrorAddition + ": " + e.getCause().getMessage());
                    } catch (InstantiationException e) {
                        throw new IllegalArgumentException("Класс '" + fieldType.getSimpleName() +
                                "' абстрактный, невозможно создать объект" + implementationErrorAddition);
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException("Ошибка вызова конструкта для типа '" + fieldType.getSimpleName()
                                + "'. Нет доступа" + implementationErrorAddition);
                    }

                    // Рекурсивный вызов метода
                    injectDependencies(dependency);
                }

                try {
                    field.set(object, dependency);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Ошибка изменения приватного поля '" + field.getName() + "'. Нет доступа");
                }

                if (configurable) {
                    ConfigProcessor.applyConfig(dependency);
                }
            }
        }
    }

    private static boolean checkFieldType(Class<?> checkingType, Class<?> fieldType) {
        if (checkingType == fieldType) return true;
        if (checkingType.getSuperclass() == fieldType) return true;
        if (Arrays.stream(checkingType.getInterfaces()).anyMatch(o -> o == fieldType)) return true;

        if (checkingType.getSuperclass() != null && checkFieldType(checkingType.getSuperclass(), fieldType)) {
            return true;
        }

        for (Class<?> i : checkingType.getInterfaces()) {
            if (checkFieldType(i, fieldType)) return true;
        }

        return false;
    }
}
