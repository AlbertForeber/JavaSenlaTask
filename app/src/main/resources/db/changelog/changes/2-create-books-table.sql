-- db/changelog/changes/2-create-books-table.sql

-- liquibase formatted sql

-- changeset books:2-create-books-table
CREATE TABLE IF NOT EXISTS books(
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    publication_date DATE NOT NULL,
    admission_date DATE NOT NULL,
    price INTEGER NOT NULL,
    status VARCHAR(10) NOT NULL,
    order_id INTEGER REFERENCES orders(id)
);
--rollback DROP TABLE books;