SELECT speed, AVG(price::numeric) FROM pc
GROUP BY speed HAVING speed>600;