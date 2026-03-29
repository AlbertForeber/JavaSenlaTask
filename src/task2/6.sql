SELECT p.maker, l.speed
FROM laptop l JOIN product p USING(model)
WHERE l.hd >= 100;
