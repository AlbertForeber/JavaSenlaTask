package com.senla.app.service.request.io;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RequestImportConstants {

    public static final Set<String> REQUIRED_FIELDS = new LinkedHashSet<>(List.of(
            "id", "bookName", "amount"
    ));
}
