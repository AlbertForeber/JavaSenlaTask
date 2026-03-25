WITH temp AS (
    SELECT maker, code, price
    FROM pc
    JOIN product USING (model)

    UNION ALL
    SELECT maker, code, price
    FROM laptop
    JOIN product USING (model)

    UNION ALL
    SELECT maker, code, price
    FROM printer
    JOIN product USING (model)
)

SELECT code, price FROM temp
WHERE maker = 'B';