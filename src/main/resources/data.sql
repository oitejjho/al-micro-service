/*
--*//*

*/
--DROP TABLE IF EXISTS billionaires;
--
--CREATE TABLE employee (
--  id INT AUTO_INCREMENT  PRIMARY KEY,
--  first_name VARCHAR(250) NOT NULL,
--  last_name VARCHAR(250) NOT NULL,
--  email VARCHAR(250) NOT NULL,
--  career VARCHAR(250) DEFAULT NULL
--);

INSERT INTO employee (first_name, last_name, email ,career) VALUES
  ('Aliko', 'Dangote', 'aliko@aliko.com', 'Billionaire Industrialist'),
  ('Bill', 'Gates', 'bill@bill.com', 'Billionaire Tech Entrepreneur'),
  ('Folrunsho', 'Alakija', 'fol@fol.com', 'Billionaire Oil Magnate');
