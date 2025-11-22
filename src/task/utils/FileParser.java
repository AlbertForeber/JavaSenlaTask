package task.utils;

import task.service.order.io.OrderImportConstants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public final class FileParser {
    public static int parseNumericField(String toParse, String fieldName) {
        try {
            return Integer.parseInt(toParse);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Поле " + fieldName + " должно быть численным");
        }
    }

    public static Map<String, ArrayList<String>> parseFile(String fileName, Set<String> allowedFields, Set<String> requiredFields) throws IOException, IllegalArgumentException {
        LinkedHashMap<String, ArrayList<String>> fields = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String curLine = br.readLine();

            if (curLine == null) throw new IllegalArgumentException("Файл '" + fileName + "' пуст");

            Arrays.stream(curLine.split(";")).forEach(x -> fields.put(x, new ArrayList<>(List.of())));
            validateFields(fields, requiredFields, allowedFields);

            while ((curLine = br.readLine()) != null) {
                processLine(curLine, fields);
            }
        }
        return fields;
    }

    // Fast-fail принцип, сразу выбрасываем ошибку (void + throw) вместо большого количества if-else блоков (bool + if-else)
    public static void validateFields(Map<String, ArrayList<String>> fields, Set<String> requiredFields, Set<String> allowedFields) {
        for (String field : requiredFields) {
            if (!fields.containsKey(field)) throw new IllegalArgumentException("Требуемое поле '" + field + "' не найдено");
        }

        Set<String> keysCopy = new HashSet<>(Set.copyOf(fields.keySet()));
        keysCopy.removeAll(allowedFields);

        if (!keysCopy.isEmpty()) throw new IllegalArgumentException("Файл содержит неизвестные поля: " + keysCopy);


    }

    private static void processLine(String line, LinkedHashMap<String, ArrayList<String>> fields) throws IllegalArgumentException {
        line = line + ";";
        List<String> values = List.of(line.split(";"));

        int i = 0;

        for (String value : fields.sequencedKeySet()) {
            if (i >= values.size()) throw new IllegalArgumentException("Строка содержит недостаточно полей\n" + line);

            fields.get(value).add(values.get(i));
            i ++;
        }

        if (i < values.size()) throw new IllegalArgumentException("Строка содержит лишние поля\n" + line);
    }
}
