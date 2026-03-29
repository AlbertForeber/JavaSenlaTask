-- db/changelog/changes/8-create-user-tables.sql

-- liquibase formatted sql

-- changeset albert:1-add-role-table
CREATE TABLE IF NOT EXISTS roles(
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL
);
-- rollback DROP TABLE roles;

-- changeset albert:2-add-scope-table
CREATE TABLE IF NOT EXISTS scopes(
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL
);
-- rollback DROP TABLE scopes;

-- changeset albert:3-add-role-scope-table
CREATE TABLE IF NOT EXISTS role_scope(
    role_id INTEGER REFERENCES roles(id),
    scope_id INTEGER REFERENCES scopes(id),
    PRIMARY KEY (role_id, scope_id)
);
-- rollback DROP TABLE role_scope;

-- changeset albert:4-add-user-table
CREATE TABLE IF NOT EXISTS users(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role_id INTEGER NOT NULL REFERENCES roles(id)
);
-- rollback DROP TABLE users;