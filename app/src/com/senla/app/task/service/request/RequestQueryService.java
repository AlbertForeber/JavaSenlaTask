package com.senla.app.task.service.request;

import com.senla.annotation.InjectTo;
import com.senla.annotation_processor.InjectProcessor;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.model.entity.Request;
import com.senla.app.task.model.entity.sortby.RequestSortBy;
import com.senla.app.task.repository.inmemory.InMemoryRequestManagerRepository;
import com.senla.app.task.service.request.io.csv.CsvRequestImportService;

import java.io.*;
import java.util.List;

public class RequestQueryService {
    @InjectTo(useImplementation = InMemoryRequestManagerRepository.class)
    private RequestManagerRepository requestManagerRepository;

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
