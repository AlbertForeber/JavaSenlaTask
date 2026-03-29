WITH all_prices AS (
    SELECT price FROM pc
    UNION ALL
    SELECT price FROM laptop
    UNION ALL
    SELECT price FROM printer
),
max_price AS (
    SELECT MAX(price) AS price
    FROM all_prices
)

SELECT code FROM pc
WHERE price = (SELECT price FROM max_price)
UNION ALL
SELECT code FROM laptop
WHERE price = (SELECT price FROM max_price)
UNION ALL
SELECT code FROM printer
WHERE price = (SELECT price FROM max_price);