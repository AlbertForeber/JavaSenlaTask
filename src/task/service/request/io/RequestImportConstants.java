package task.service.request.io;

import java.util.Set;

public class RequestImportConstants {
    public static final Set<String> REQUIRED_FIELDS = Set.of(
            "id", "bookName", "amount"
    );
}
