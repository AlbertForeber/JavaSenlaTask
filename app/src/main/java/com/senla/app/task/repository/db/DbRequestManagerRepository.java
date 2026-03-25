package com.senla.app.task.repository.db;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.task.db.dao.GenericDao;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.Request;
import com.senla.app.task.model.entity.sortby.OrderSortBy;
import com.senla.app.task.model.entity.sortby.RequestSortBy;
import com.senla.app.task.repository.RequestManagerRepository;
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
    public void addRequest(int requestId, Book book) {
        try {
            requestDao.save(new Request(requestId, book, 1));
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public void addRequest(Book book) {
        try {
            List<Request> alreadyIn = requestDao.findAll(null, true).stream().filter(o -> Objects.equals(o.getBook().getTitle(), book.getTitle())).toList();

            if (!alreadyIn.isEmpty()) {
                Request toUpdate = alreadyIn.getFirst();
                toUpdate.incrementAmount();

                requestDao.update(toUpdate);
            } else
                requestDao.save(new Request(0, book, 1));
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public Request getRequest(int requestId) {
        try {
            return requestDao.findById(requestId, true);
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public void cancelRequests(String bookName) {
        try {

            for (Request request : requestDao.findAll(null, true)) {
                if (Objects.equals(request.getBook().getTitle(), bookName))
                    requestDao.delete(request.getId());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public List<Request> getSortedRequests(RequestSortBy sortBy) {
        try {
            return requestDao.findAll(sortBy != RequestSortBy.NO_SORT ? sortBy : null, true);
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }
}
