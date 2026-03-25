-- db/changelog/changes/7-fill-book-order.sql

-- liquibase formatted sql

-- changeset fill-book-order:7-fill-book-order
INSERT INTO book_order
VALUES
    (6, 1),
    (7, 2),
    (8, 3),
    (10, 4);
--rollback DELETE FROM book_order;