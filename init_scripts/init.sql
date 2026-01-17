CREATE TABLE IF NOT EXISTS orders(
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    total_sum INTEGER NOT NULL,
    completion_date DATE,
    status VARCHAR(10) NOT NULL
);

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

CREATE TABLE IF NOT EXISTS requests(
    id SERIAL PRIMARY KEY,
    book_id INTEGER REFERENCES books(id),
    amount INTEGER NOT NULL
);


INSERT INTO orders (customer_name, total_sum, completion_date, status)
VALUES
    ('reservist6', 500, NULL, 'NEW'),
    ('reservist7', 303, NULL, 'NEW'),
    ('reservist8', 400, NULL, 'NEW'),
    ('reservist10', 200, NULL, 'NEW');

INSERT INTO books (title, description, publication_date, admission_date, price, status, order_id)
VALUES
    ('I_Book1', 'Desc1', '1952-1-2', '2025-1-1', 700, 'FREE', NULL),
    ('G_Book2', 'Desc2', '1983-2-17', '2025-2-2', 100, 'FREE', NULL),
    ('F_Book3', 'Desc3', '1980-1-11', '2025-3-3', 301, 'FREE', NULL),
    ('H_Book4', 'Desc4', '1931-1-1', '2025-4-4', 800, 'FREE', NULL),
    ('C_Book5', 'Desc5', '1929-1-6', '2025-5-5', 302, 'FREE', NULL),
    ('D_Book6', 'Desc6', '1918-9-8', '2025-6-6', 500, 'RESERVED', 1),
    ('E_Book7', 'Desc7', '1964-1-12', '2025-7-7', 303, 'RESERVED', 2),
    ('B_Book8', 'Desc8', '1928-1-20', '2025-8-8', 400, 'RESERVED', 3),
    ('A_Book9', 'Desc8', '1918-1-18', '2025-9-9', 801, 'FREE', NULL),
    ('J_Book10', 'Desc10', '1993-1-10', '2025-10-10', 200, 'RESERVED', 4);