SELECT DISTINCT maker FROM product
JOIN pc USING(model)
WHERE speed>450;