SELECT AVG(speed) FROM pc
JOIN product p USING(model)
WHERE maker='A';