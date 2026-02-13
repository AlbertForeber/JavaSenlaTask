package com.senla.app.repository.db;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.db.DatabaseException;
import com.senla.app.db.dao.GenericDao;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.Request;
import com.senla.app.model.entity.sortby.RequestSortBy;
import com.senla.app.repository.RequestManagerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@Db
public class DbRequestManagerRepository implements RequestManagerRepository {

    private final GenericDao<Request, Integer, RequestSortBy> requestDao;

    public DbRequestManagerRepository(@Hibernate GenericDao<Request, Integer, RequestSortBy> requestDao) {
        this.requestDao = requestDao;
    }

    @Override
    public void addRequest(int requestId, Book book) throws DatabaseException {
        requestDao.save(new Request(requestId, book, 1));
    }

    @Override
    public Request addRequest(Book book) throws DatabaseException {
        List<Request> alreadyIn = requestDao.findAll(null, true).stream().filter(o -> Objects.equals(o.getBook().getTitle(), book.getTitle())).toList();

        if (!alreadyIn.isEmpty()) {
            Request toUpdate = alreadyIn.getFirst();
            toUpdate.incrementAmount();

            requestDao.update(toUpdate);
            return toUpdate;
        } else {
            return requestDao.save(new Request(0, book, 1));
        }
    }

    @Override
    public Request getRequest(int requestId) throws DatabaseException {
        return requestDao.findById(requestId, true);
    }

    @Override
    public void cancelRequests(String bookName) throws DatabaseException {
        for (Request request : requestDao.findAll(null, true)) {
            if (Objects.equals(request.getBook().getTitle(), bookName))
                requestDao.delete(request.getId());
        }
    }

    @Override
    public List<Request> getSortedRequests(RequestSortBy sortBy) throws DatabaseException {
        return requestDao.findAll(sortBy != RequestSortBy.NO_SORT ? sortBy : null, true);
    }
}
