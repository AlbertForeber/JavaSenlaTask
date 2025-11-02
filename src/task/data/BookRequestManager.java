package task.data;

import task.data.comparators.order.OrderComplDateComparator;
import task.data.comparators.order.OrderPriceComparator;
import task.data.comparators.order.OrderStatusComparator;
import task.data.comparators.request.RequestAmountComparator;
import task.data.comparators.request.RequestBookNameComparator;
import task.data.dto.Order;
import task.data.dto.Request;
import task.data.dto.sortby.OrderSortBy;
import task.data.dto.sortby.RequestSortBy;
import task.domain.RequestManager;

import java.util.*;

public class BookRequestManager implements RequestManager {
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
        return requests;
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
