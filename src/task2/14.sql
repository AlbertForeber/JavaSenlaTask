SELECT speed, AVG(price::numeric) FROM pc
GROUP BY speed;