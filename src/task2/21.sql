SELECT maker, MAX(price) FROM product
JOIN pc USING(model)
GROUP BY maker;