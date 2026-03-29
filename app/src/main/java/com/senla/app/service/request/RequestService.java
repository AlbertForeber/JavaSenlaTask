package com.senla.app.service.request;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.exceptions.WrongId;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.Request;
import com.senla.app.repository.RequestManagerRepository;
import com.senla.app.repository.StorageRepository;
import com.senla.app.service.unit_of_work.UnitOfWork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

    private final RequestManagerRepository requestManagerRepository;

    private final StorageRepository storageRepository;

    private final UnitOfWork unitOfWork;

    public RequestService(
            @Db RequestManagerRepository requestManagerRepository,
            @Db StorageRepository storageRepository,
            @Hibernate UnitOfWork unitOfWork) {
        this.requestManagerRepository = requestManagerRepository;
        this.storageRepository = storageRepository;
        this.unitOfWork = unitOfWork;
    }

    @Transactional
    public Request createRequest(int bookId) {
        Book book = storageRepository.getBook(bookId, false);

        if (book == null) throw new WrongId("книга с артикулом #" + bookId + " не найдена");

        return requestManagerRepository.addRequest(book);
    }
}
