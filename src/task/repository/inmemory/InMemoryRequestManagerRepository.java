package task.repository.inmemory;

import task.repository.RequestManagerRepository;
import task.model.comparators.request.RequestAmountComparator;
import task.model.comparators.request.RequestBookNameComparator;
import task.model.entity.Request;
import task.model.entity.sortby.RequestSortBy;

import java.util.*;

public class InMemoryRequestManagerRepository implements RequestManagerRepository {
    private final LinkedHashMap<Integer, Request> requests = new LinkedHashMap<>();

    @Override
    public void addRequest(int requestId, String bookName) {
        if (requests.containsKey(requestId)) requests.get(requestId).incrementAmount();
        else requests.put(requestId, new Request(requestId, bookName));
    }

    @Override
    public void addRequest(String bookName) {

        int lastId = 0;

        for (Request request : requests.values()) {
            if (Objects.equals(request.getBookName(), bookName)) {
                request.incrementAmount();
                return;
            }
            lastId = request.getId();
        }
        requests.put(lastId + 1, new Request(lastId + 1, bookName));

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
    public List<Request> getRequests() {
        // Возвращаем копию для безопасности
        return new ArrayList<>(requests.values());
    }

    @Override
    public List<Request> getSortedRequests(RequestSortBy sortBy) {
        Comparator<Request> comparator = switch (sortBy) {
            case AMOUNT -> new RequestAmountComparator();
            case BOOK_NAME -> new RequestBookNameComparator();
            case NO_SORT -> null;
        };

        List<Request> arr = new ArrayList<>(requests.values());

        if (comparator != null) arr.sort(comparator);
        return arr;
    }

    @Override
    public String toString() {
        return requests.toString();
    }

}
