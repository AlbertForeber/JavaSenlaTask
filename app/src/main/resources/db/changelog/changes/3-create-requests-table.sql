-- db/changelog/changes/3-create-orders-table.sql

-- liquibase formatted sql

-- changeset requests:3-create-orders-table
CREATE TABLE IF NOT EXISTS requests(
    id SERIAL PRIMARY KEY,
    book_id INTEGER REFERENCES books(id),
    amount INTEGER NOT NULL
);
--rollback DROP TABLE requests;