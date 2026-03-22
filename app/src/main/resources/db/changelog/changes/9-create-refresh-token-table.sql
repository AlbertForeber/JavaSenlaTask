-- db/changelog/changes/9-create-refresh-token-table.sql

-- liquibase formatted sql

-- changeset albert:1-create-table
CREATE TABLE IF NOT EXISTS refresh_tokens(
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE,
    user_id INTEGER REFERENCES users(id),
    expiry_date TIMESTAMP,
    used BOOLEAN,
    revoked BOOLEAN,
    created_at TIMESTAMP,
    replaced_by_token VARCHAR(255)
);

-- rollback DROP TABLE refresh_token;