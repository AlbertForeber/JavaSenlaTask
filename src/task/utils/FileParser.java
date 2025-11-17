package task.utils;

import task.service.order.io.OrderImportConstants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileParser {
    public static int parseNumericField(String toParse, String fieldName) {
        try {
            return Integer.parseInt(toParse);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Поле " + fieldName + " должно быть численным");
        }
    }

    public static Map<String, String> parseFile(String fileName, Set<String> allowedFields) throws IOException, IllegalArgumentException {
        Map<String, String> fields = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String curLine;

            while ((curLine = br.readLine()) != null) {
                processLine(curLine, fields, allowedFields);
            }
        }
        return fields;
    }

    // Fast-fail принцип, сразу выбрасываем ошибку (void + throw) вместо большого количества if-else блоков (bool + if-else)
    public static void validateRequiredFields(Map<String, String> fields, Set<String> requiredFields) {
        for (String field : requiredFields) {
            if (!fields.containsKey(field)) throw new IllegalArgumentException("Требуемое поле '" + field + "' не найдено");
        }
    }

    private static void processLine(String line, Map<String, String> fields, Set<String> allowedFields) throws IllegalArgumentException {
        List<String> keyValue = List.of(line.split(";"));
        if (keyValue.size() != 2) throw new IllegalArgumentException("Неверный формат строки '" + line + "'");

        String key = keyValue.getFirst().trim();
        String value = keyValue.getLast().trim();

        if (fields.containsKey(key)) throw new IllegalArgumentException("Дубликат поля '" + key + "'");
        if (!allowedFields.contains(key)) throw new IllegalArgumentException("Неизвестное поле '" + key + "'");

        fields.put(key, value);

    }

}
