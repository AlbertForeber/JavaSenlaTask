package task.service.request;

import task.model.entity.Order;
import task.model.entity.sortby.OrderSortBy;
import task.repository.RequestManagerRepository;
import task.model.entity.Request;
import task.model.entity.sortby.RequestSortBy;

import java.io.*;
import java.util.List;

public class RequestQueryService {
    private final RequestManagerRepository requestManagerRepository;

    public RequestQueryService(
            RequestManagerRepository requestManagerRepository
    ) {
        this.requestManagerRepository = requestManagerRepository;
    }

    public List<Request> getSorted(RequestSortBy sortBy) {
        return requestManagerRepository.getSortedRequests(sortBy);
    }

    public void saveState(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path + "request"))) {
            for (Request request : requestManagerRepository.getSortedRequests(RequestSortBy.NO_SORT)) {
                oos.writeObject(request);
            }
        }
    }

    public void loadState(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path + "request"))) {
            Request request;
            while (true) {
                try {
                    request = (Request) ois.readObject();
                    requestManagerRepository.addRequest(request.getId(), request.getBookName());
                    requestManagerRepository.getRequest(request.getId()).setAmount(request.getAmount());
                } catch (EOFException e) {
                    break;
                }
            }


        }
    }
}
