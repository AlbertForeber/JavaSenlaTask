package com.senla.app.model.dto.response;

import java.util.ArrayList;
import java.util.List;

public class CollectionResponse<T> {

    private final List<T> objects = new ArrayList<>();

    public CollectionResponse(Iterable<T> collection) {
        for (T object : collection) {
            objects.add(object);
        }
    }

    public List<T> getObjects() {
        return objects;
    }
}
