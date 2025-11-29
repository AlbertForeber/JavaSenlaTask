import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class ConfigProcessor {

    private ConfigProcessor() {}

    public static void applyConfig(Object object) throws IllegalArgumentException {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            Properties properties;

            // Проверка, что нужная аннотация найдена
            if (annotation != null) {
                try {
                    properties = PropertyLoader.loadProperties(annotation.configFileName());
                } catch (IOException e) {
                    throw new IllegalArgumentException("Конфиг с названием '" + annotation.configFileName() + "' не найден");
                }

                // Используем значение по умолчанию для имени поля, если не указано в аннотации
                String propertyName = annotation.propertyName().trim().isEmpty() ?
                        annotation.propertyName() : clazz.getSimpleName() + field.getName();


                String value = properties.getProperty(propertyName);
                if (value == null) {
                    throw new IllegalArgumentException("Поля с именем '" + propertyName + "' не найдено в указанном конфиг-файле.");
                }


                if (field.getType() != annotation.type()) {
                    throw new IllegalArgumentException("Указанный тип '" + annotation.type().getSimpleName() + "' не совпадает с типом поля");
                }

                // Используем метод без исключений
                field.trySetAccessible();

                try {
                    field.set(object, annotation.type().cast(value));
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Ошибка изменения приватного поля '" + field.getName() + "'. Нет доступа");
                }
            }
        }
    }
}
