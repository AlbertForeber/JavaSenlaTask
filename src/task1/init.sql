CREATE TABLE product (
    model VARCHAR(50) PRIMARY KEY,
    maker VARCHAR(10) NOT NULL,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE pc (
    code INTEGER PRIMARY KEY,
    model VARCHAR(50) NOT NULL REFERENCES pc(model),
    speed SMALLINT NOT NULL,
    ram SMALLINT NOT NULL,
    hd REAL NOT NULL,
    cd VARCHAR(10) NOT NULL,
    price MONEY
);

CREATE TABLE laptop (
    code INTEGER PRIMARY KEY,
    model VARCHAR(50) NOT NULL REFERENCES pc(model),
    speed SMALLINT NOT NULL,
    ram SMALLINT NOT NULL,
    hd REAL NOT NULL,
    screen SMALLINT NOT NULL,
    price MONEY
);

CREATE TABLE printer (
    code INTEGER PRIMARY KEY,
    model VARCHAR(50) NOT NULL REFERENCES pc(model),
    color CHAR(1) NOT NULL,
    type VARCHAR(10) NOT NULL,
    price MONEY
);

INSERT INTO product (maker, model, type) VALUES
('A', 'PC_1', 'PC'),
('A', 'PC_2', 'PC'),
('A', 'PC_3', 'PC');

INSERT INTO pc (code, model, speed, ram, hd, cd, price) VALUES
(1, 'PC_1', 600, 8, 500, '12x', 450),
(2, 'PC_2', 600, 8, 500, '24x', 650),
(3, 'PC_3', 600, 16, 1000, '24x', 700);

INSERT INTO product (maker, model, type) VALUES
('B', 'PC_B1', 'PC'),
('B', 'L_B1', 'Laptop'),
('B', 'PR_B1', 'Printer');

INSERT INTO pc (code, model, speed, ram, hd, cd, price) VALUES
(4, 'PC_B1', 800, 4, 256, '24x', 800);

INSERT INTO laptop (code, model, speed, ram, hd, price, screen) VALUES
(5, 'L_B1', 750, 8, 512, 1200, 15);

INSERT INTO printer (code, model, color, type, price) VALUES
(6, 'PR_B1', 'y', 'Laser', 300);

INSERT INTO product (maker, model, type) VALUES
('C', 'PR_C1', 'Printer');

INSERT INTO printer (code, model, color, type, price) VALUES
(7, 'PR_C1', 'n', 'Jet', 150);

INSERT INTO product (maker, model, type) VALUES
('D', 'PC_D1', 'PC'),
('D', 'PC_D2', 'PC');

INSERT INTO pc (code, model, speed, ram, hd, cd, price) VALUES
(8, 'PC_D1', 700, 16, 1000, '24x', 900),
(9, 'PC_D2', 700, 16, 1000, '24x', 950);

INSERT INTO product (maker, model, type) VALUES
('E', 'L_E1', 'Laptop');

INSERT INTO laptop (code, model, speed, ram, hd, price, screen) VALUES
(10, 'L_E1', 500, 4, 120, 1100, 14);



