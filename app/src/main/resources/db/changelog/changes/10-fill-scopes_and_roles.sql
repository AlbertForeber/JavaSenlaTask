-- db/changelog/changes/10-fill-scopes_and_roles.sql

-- liquibase formatted sql

-- changeset albert:1-fill-scopes-table
INSERT INTO scopes VALUES
    (1, 'order:create'),
    (2, 'order:cancel'),
    (3, 'order:change_status'),
    (4, 'order:view_all'),
    (5, 'order:view_completed'),
    (6, 'order:stats'),
    (7, 'order:details'),
    (8, 'request:add'),
    (9, 'request:view_all'),
    (10, 'book:write_off'),
    (11, 'book:add'),
    (12, 'book:view_all'),
    (13, 'book:dead_stock'),
    (14, 'book:view_description'),
    (15, 'order:cancel_all');
-- rollback DELETE FROM scopes;

-- changeset albert:2-fill-roles-table
INSERT INTO roles VALUES
   (1, 'customer'),
   (2, 'employee');
-- rollback DELETE FROM roles;

-- changeset albert:3-fill-role-scope-table
INSERT INTO role_scope VALUES
    (1, 1),
    (1, 2),
    (1, 7),
    (1, 12),
    (1, 14),
    (2, 1),
    (2, 2),
    (2, 3),
    (2, 4),
    (2, 5),
    (2, 6),
    (2, 7),
    (2, 8),
    (2, 9),
    (2, 10),
    (2, 11),
    (2, 12),
    (2, 13),
    (2, 14),
    (2, 15);
-- rollback DELETE FROM role_scope;