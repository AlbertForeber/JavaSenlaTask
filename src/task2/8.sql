SELECT DISTINCT maker FROM product
WHERE type='PC'
EXCEPT
SELECT DISTINCT maker FROM product
WHERE type='Laptop';