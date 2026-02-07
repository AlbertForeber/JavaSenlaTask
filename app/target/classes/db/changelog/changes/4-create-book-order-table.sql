-- db/changelog/changes/4-create-book-order-table.sql

-- liquibase formatted sql

-- changeset books-order:4-create-book-order-table
CREATE TABLE IF NOT EXISTS book_order(
    book_id INT REFERENCES books(id),
    order_id INT REFERENCES orders(id),
    PRIMARY KEY (book_id, order_id)
);
--rollback DROP TABLE book_order;