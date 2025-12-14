SELECT DISTINCT maker FROM product p
JOIN pc USING(model)
WHERE speed=(
    SELECT MAX(speed) FROM pc WHERE ram = (
        SELECT MIN(ram) FROM pc
    )
)
AND
p.maker IN (
    SELECT maker FROM product p
    WHERE type='Printer'
);