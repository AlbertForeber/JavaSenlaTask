SELECT p1.code, p2.code, p1.speed, p1.ram
FROM pc p1
JOIN pc p2
  ON p1.ram = p2.ram
 AND p1.speed = p2.speed
 AND p1.code > p2.code;