package com.senla.app.task.service.order.io;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// Добавили константы для обозначения доступных в order полей для лучшей гибкости кода
public class OrderImportConstants {
    public static final Set<String> REQUIRED_FIELDS = new LinkedHashSet<>(List.of(
            "id", "bookIds", "customerName", "status"
    ));

    public static final Set<String> ALLOWED_FIELDS = new LinkedHashSet<>(List.of(
            "id", "bookIds", "customerName", "totalSum", "completionDate", "status"
    ));
}
