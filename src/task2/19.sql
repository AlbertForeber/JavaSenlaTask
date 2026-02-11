SELECT maker, AVG(screen) FROM laptop
JOIN product USING(model)
GROUP BY maker;