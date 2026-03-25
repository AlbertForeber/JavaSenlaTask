-- db/changelog/changes/5-fill-orders.sql

-- liquibase formatted sql

-- changeset fill-orders:5-fill-orders
INSERT INTO orders (customer_name, total_sum, completion_date, status)
VALUES
    ('reservist6', 500, NULL, 'NEW'),
    ('reservist7', 303, NULL, 'NEW'),
    ('reservist8', 400, NULL, 'NEW'),
    ('reservist10', 200, NULL, 'NEW');
--rollback DELETE FROM orders;