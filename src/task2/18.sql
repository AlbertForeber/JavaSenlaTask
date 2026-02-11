SELECT maker, price FROM printer
JOIN product USING (model)
WHERE price = (SELECT MIN(price) FROM printer WHERE color='y');