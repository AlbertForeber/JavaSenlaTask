package task.service.domain;

import task.model.comparators.request.RequestAmountComparator;
import task.model.comparators.request.RequestBookNameComparator;
import task.model.entity.Request;
import task.model.entity.sortby.RequestSortBy;

import java.util.*;

public class BookRequestManagerService implements RequestManagerService {
    private final List<Request> requests = new ArrayList<>();

    @Override
    public void addRequest(Request request) {
        int sameRequestIndex = requests.indexOf(request);

        if (sameRequestIndex != -1) requests.get(sameRequestIndex).incrementAmount();
        else requests.add(request);
    }

    @Override
    public void cancelRequests(String bookName) {
        requests.removeIf(r -> Objects.equals(r.getBookName(), bookName));
    }

    @Override
    public List<Request> getRequests() {
        // Возвращаем копию для безопасности
        return new ArrayList<>(requests);
    }

    @Override
    public Request[] getSortedRequests(RequestSortBy sortBy) {
        Comparator<Request> comparator = switch (sortBy) {
            case AMOUNT -> new RequestAmountComparator();
            case BOOK_NAME -> new RequestBookNameComparator();
            case NO_SORT -> null;
        };

        Request[] arr = requests.toArray(new Request[0]);
        if (comparator != null) Arrays.sort(arr, comparator);

        return arr;
    }

    @Override
    public String toString() {
        return requests.toString();
    }

}
