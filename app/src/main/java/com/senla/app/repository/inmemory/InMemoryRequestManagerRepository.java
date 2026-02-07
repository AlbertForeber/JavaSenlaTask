package com.senla.app.repository.inmemory;

import com.senla.annotation.repo_qualifiers.InMemory;
import com.senla.app.model.entity.Book;
import com.senla.app.repository.RequestManagerRepository;
import com.senla.app.model.comparators.request.RequestAmountComparator;
import com.senla.app.model.comparators.request.RequestBookNameComparator;
import com.senla.app.model.entity.Request;
import com.senla.app.model.entity.sortby.RequestSortBy;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@InMemory
public class InMemoryRequestManagerRepository implements RequestManagerRepository {

    private final LinkedHashMap<Integer, Request> requests = new LinkedHashMap<>();

    @Override
    public void addRequest(int requestId, Book book) {
        if (requests.containsKey(requestId)) requests.get(requestId).incrementAmount();
        else requests.put(requestId, new Request(requestId, book));
    }

    @Override
    public void addRequest(Book book) {

        int lastId = 0;

        for (Request request : requests.values()) {
            if (Objects.equals(request.getBook(), book)) {
                request.incrementAmount();
                return;
            }
            lastId = request.getId();
        }
        requests.put(lastId + 1, new Request(lastId + 1, book));
    }


    @Override
    public Request getRequest(int requestId) {
        return requests.get(requestId);
    }

    @Override
    public void cancelRequests(String bookName) {
        //requests.removeIf(r -> Objects.equals(r.getBookName(), bookName));
        for (Request request : requests.values()) {
            if (Objects.equals(request.getBook().getTitle(), bookName))
                requests.remove(request.getId());
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
