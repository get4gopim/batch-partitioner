CREATE TABLE emp.customer (
id serial PRIMARY KEY,
firstName VARCHAR(255) NULL,
lastName VARCHAR(255) NULL,
birthdate date not null
);

CREATE TABLE emp.new_customer (
id serial PRIMARY KEY,
firstName VARCHAR(255) NULL,
lastName VARCHAR(255) NULL,
birthdate date not null
);

INSERT INTO emp.customer VALUES 
('1', 'John', 'Doe', '1985-10-10'),
('2', 'Amy', 'Eugene', '1985-05-07'),
('3', 'Laverne', 'Mann', '1988-11-12'),
('4', 'Janice', 'Preston', '1985-09-02'),
('5', 'Pauline', 'Rios', '1985-08-29'),
('6', 'Perry', 'Burnside', '1981-10-03'),
('7', 'Todd', 'Kinsey', '1998-11-22'),
('8', 'Jacqueline', 'Hyde', '1988-12-03'),
('9', 'Rico', 'Hale', '2000-10-10'),
('10', 'Samuel', 'Lamm', '1999-11-11'),
('11', 'Robert', 'Coster', '1982-10-10'),
('12', 'Tamara', 'Soler', '1982-02-01'),
('13', 'Justin', 'Kramer', '1982-09-11'),
('14', 'Andrea', 'Law', '1988-10-10'),
('15', 'Laura', 'Porter', '2010-12-12'),
('16', 'Michael', 'Cantu', '1999-11-04'),
('17', 'Andrew', 'Thomas', '1999-04-05'),
('18', 'Jose', 'Hannah', '1999-09-16'),
('19', 'Valerie', 'Hilbert', '1988-12-06'),
('20', 'Patrick', 'Durham', '1982-12-10');

INSERT INTO emp.customer VALUES
('21', 'Mike', 'Doe', '1982-10-10'),
('22', 'Aloy', 'Eugene', '1985-05-07'),
('23', 'Whonc', 'Mann', '1988-11-12'),
('24', 'Hack', 'Preston', '1985-09-02'),
('25', 'Mackinson', 'Rios', '1985-08-29'),
('26', 'Joul', 'Burnside', '1988-10-03'),
('27', 'Iopsn', 'Kinsey', '1998-11-22'),
('28', 'Mecy', 'Hyde', '1983-12-03'),
('29', 'Jersy', 'Hale', '2000-10-10'),
('30', 'Marisa', 'Weekler', '1999-11-11'),
('31', 'Hanna', 'Coster', '1982-10-10'),
('32', 'John', 'Soler', '1988-02-01'),
('33', 'Pursin', 'Kramer', '1981-09-11'),
('34', 'Adrain', 'Law', '1981-10-10'),
('35', 'Ipsam', 'Porter', '2010-12-12'),
('36', 'Kohler', 'Cantu', '1999-11-04'),
('37', 'Bulgara', 'Thomas', '1967-04-05'),
('38', 'Mandarian', 'Hannah', '1967-09-16'),
('39', 'Chinesan', 'Hilbert', '1966-12-06'),
('40', 'Indiana', 'Johnes', '1981-12-10');