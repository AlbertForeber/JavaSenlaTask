package com.senla.app.task.repository.inmemory;

import com.senla.app.task.model.entity.Book;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.model.comparators.request.RequestAmountComparator;
import com.senla.app.task.model.comparators.request.RequestBookNameComparator;
import com.senla.app.task.model.entity.Request;
import com.senla.app.task.model.entity.sortby.RequestSortBy;

import java.util.*;

public class InMemoryRequestManagerRepository implements RequestManagerRepository {
    private final LinkedHashMap<Integer, Request> requests = new LinkedHashMap<>();

    @Override
    public void addRequest(int requestId, String bookName) {
        if (requests.containsKey(requestId)) requests.get(requestId).incrementAmount();
        else requests.put(requestId, new Request(requestId, bookName));
    }

    @Override
    public void addRequest(Book book) {

        int lastId = 0;

        for (Request request : requests.values()) {
            if (Objects.equals(request.getBookName(), book.getTitle())) {
                request.incrementAmount();
                return;
            }
            lastId = request.getId();
        }
        requests.put(lastId + 1, new Request(lastId + 1, book.getTitle()));

    }


    @Override
    public Request getRequest(int requestId) {
        return requests.get(requestId);
    }

    @Override
    public void cancelRequests(String bookName) {
        //requests.removeIf(r -> Objects.equals(r.getBookName(), bookName));
        for (Request request : requests.values()) {
            if (Objects.equals(request.getBookName(), bookName)) requests.remove(request.getId());
        }
    }

    @Override
    public List<Request> getSortedRequests(RequestSortBy sortBy) {
        Comparator<Request> comparator = switch (sortBy) {
            case AMOUNT -> new RequestAmountComparator();
            case BOOK_NAME -> new RequestBookNameComparator();
            case NO_SORT -> null;
        };


        // Возвращаем копию для безопасности
        List<Request> arr = new ArrayList<>(requests.values());

        if (comparator != null) arr.sort(comparator);
        return arr;
    }

    @Override
    public String toString() {
        return requests.toString();
    }

}
