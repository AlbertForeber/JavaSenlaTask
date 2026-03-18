-- db/changelog/changes/9-create-refresh-token-table.sql

-- liquibase formatted sql

-- changeset albert:1-create-table
CREATE TABLE IF NOT EXISTS refresh_token(
    id SERIAL PRIMARY KEY,
    token VARCHAR(256) UNIQUE,
    username VARCHAR(100),
    expiry_date DATE,
    used BOOLEAN,
    revoked BOOLEAN,
    created_at DATE,
    replaced_by_token VARCHAR(256) UNIQUE
);

-- rollback DROP TABLE refresh_token;