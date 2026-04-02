SELECT DISTINCT maker FROM product p
WHERE EXISTS (
    SELECT 1 FROM product p1
    JOIN pc USING (model)
    WHERE p1.maker = p.maker AND type='PC' AND speed>=750
) AND EXISTS (
    SELECT 1 FROM product p1
    JOIN laptop USING (model)
    WHERE p1.maker = p.maker AND type='Laptop' AND speed>=750
);