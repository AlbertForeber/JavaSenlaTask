-- db/changelog/changes/1-create-orders-table.sql

-- liquibase formatted sql

-- changeset orders:1-create-orders-table
CREATE TABLE IF NOT EXISTS orders(
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    total_sum INTEGER NOT NULL,
    completion_date DATE,
    status VARCHAR(10) NOT NULL
);
--rollback DROP TABLE orders;