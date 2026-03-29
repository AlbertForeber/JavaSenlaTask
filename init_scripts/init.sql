CREATE TABLE IF NOT EXISTS accounts(
    id SERIAL PRIMARY KEY,
    balance INTEGER
);

CREATE TABLE IF NOT EXISTS transfers(
    id SERIAL PRIMARY KEY,
    from_account INTEGER REFERENCES accounts(id),
    to_account INTEGER REFERENCES accounts(id),
    transfer_amount INTEGER,
    status VARCHAR(50) CHECK (status IN ('SUCCESS', 'ERROR'))
);
