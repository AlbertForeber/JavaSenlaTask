-- db/changelog/changes/10-fill-scopes_and_roles.sql

-- liquibase formatted sql

-- changeset albert:1-fill-role-scope-table
INSERT INTO users VALUES (1, 'customer1', '$2a$10$GhqKmKI.Aw/w6HXB8hYqtubUHpFAdN//4SuVZW1ZRQq6z6kwursM6', 1);
INSERT INTO users VALUES (2, 'employee1', '$2a$10$fPzq6um52UNo7v/iQc3aHOgZg.b9EwbcGt7/29DMBdTngVy0wMLfC', 2);
-- rollback DELETE FROM role_scope;