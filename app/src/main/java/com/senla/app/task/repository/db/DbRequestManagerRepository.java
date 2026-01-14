package com.senla.app.task.repository.db;

import com.senla.annotation.InjectTo;
import com.senla.app.task.db.dao.implementations.BookDao;
import com.senla.app.task.db.dao.implementations.RequestDao;
import com.senla.app.task.model.dto.RequestDto;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.Request;
import com.senla.app.task.model.entity.sortby.RequestSortBy;
import com.senla.app.task.repository.RequestManagerRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class DbRequestManagerRepository implements RequestManagerRepository {

    @InjectTo
    RequestDao requestDao;

    @InjectTo
    BookDao bookDao;

    @Override
    public void addRequest(int requestId, String bookName) {
        try {
            int bookId = getBookIdByName(bookName);
            requestDao.save(new RequestDto(requestId, bookId, bookName, 1));
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public void addRequest(Book book) {
        try {
            List<RequestDto> alreadyIn = requestDao.findAll("").stream().filter(o -> Objects.equals(o.getBookName(), book.getTitle())).toList();

            if (!alreadyIn.isEmpty()) {
                RequestDto toUpdate = alreadyIn.getFirst();
                requestDao.update(new RequestDto(toUpdate.getId(), toUpdate.getBookId(), book.getTitle(), toUpdate.getAmount() + 1));
            } else
                requestDao.save(new RequestDto(book.getId(), book.getTitle(), 1));
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public Request getRequest(int requestId) {
        try {
            RequestDto requestDto = requestDao.findById(requestId);

            if (requestDto == null) return null;
            return requestDto.toBusinessObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public void cancelRequests(String bookName) {
        try {

            for (RequestDto requestDto : requestDao.findAll("")) {
                if (Objects.equals(requestDto.getBookName(), bookName))
                    requestDao.delete(requestDto.getId());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public List<Request> getSortedRequests(RequestSortBy sortBy) {
        StringBuilder additionSortQuery = new StringBuilder("ORDER BY ");

        switch (sortBy) {
            case BOOK_NAME -> additionSortQuery.append("title");
            case AMOUNT -> additionSortQuery.append("amount");
        }

        try {
            return requestDao.findAll(
                    sortBy != RequestSortBy.NO_SORT ? additionSortQuery.toString() : ""
            ).stream().map(RequestDto::toBusinessObject).toList();
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    private int getBookIdByName(String bookName) throws SQLException {
        return bookDao
                .findAll("")
                .stream()
                .dropWhile(o -> !Objects.equals(o.getTitle(), bookName))
                .toList().getFirst().getId();
    }

    private String getBookNameFromDto(RequestDto requestDto) throws SQLException {
        return bookDao.findById(requestDto.getBookId()).getTitle();
    }
}
