package task.service.storage.io;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedSet;
import java.util.Set;

public class BookImportConstants {
    public static final Set<String> REQUIRED_FIELDS = new LinkedHashSet<>(List.of(
            "id", "title", "description", "publicationDate", "admissionDate", "price", "status"
    ));

    public static final Set<String> ALLOWED_FIELDS = new LinkedHashSet<>(List.of(
            "id", "title", "description", "publicationDate", "admissionDate", "price", "status", "reservist"
    ));
}
