package com.senla.app.controller;

import com.senla.annotation.LoggingOperation;
import com.senla.app.model.dto.response.CollectionResponse;
import com.senla.app.model.dto.response.RequestResponse;
import com.senla.app.model.entity.Request;
import com.senla.app.model.entity.sortby.RequestSortBy;
import com.senla.app.service.request.RequestQueryService;
import com.senla.app.service.request.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;
    private final RequestQueryService requestQueryService;

    public RequestController(
            RequestService requestService,
            RequestQueryService requestQueryService
    ) {
        this.requestService = requestService;
        this.requestQueryService = requestQueryService;
    }

    @PostMapping("/add")
    @LoggingOperation("добавление запроса")
    public ResponseEntity<RequestResponse> addRequests(
            @RequestParam Integer bookId
    ) {
        Request request = requestService.createRequest(bookId);
        URI url = URI.create("/api/requests/" + request.getId());

        return ResponseEntity.created(url).body(new RequestResponse(request));
    }

    @GetMapping({"/", ""})
    @LoggingOperation("получение отсортированных запросов")
    public ResponseEntity<CollectionResponse<RequestResponse>> getSorted(
            @RequestParam(defaultValue = "NO_SORT") RequestSortBy sort
    ) {
        List<Request> sortedRequests = requestQueryService.getSorted(sort);
        return ResponseEntity.ok(new CollectionResponse<>(sortedRequests.stream().map(RequestResponse::new).toList()));
    }
}
