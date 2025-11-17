package task.service.storage.io;

import java.util.Set;

public class BookImportConstants {
    public static final Set<String> REQUIRED_FIELDS = Set.of(
            "id", "title", "description", "publicationDate", "admissionDate", "price", "status"
    );

    public static final Set<String> ALLOWED_FIELDS = Set.of(
            "id", "title", "description", "publicationDate", "admissionDate", "price", "status"
    );
}
