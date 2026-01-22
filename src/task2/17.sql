SELECT type, model, speed FROM laptop
JOIN product USING(model)
WHERE speed < (SELECT MIN(speed) FROM pc);