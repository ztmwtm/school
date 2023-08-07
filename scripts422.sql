CREATE TABLE human
(
    id             INTEGER PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    driver_licence BOOLEAN DEFAULT FALSE

);
CREATE TABLE car
(
    id    INTEGER PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    price NUMERIC(15, 5)
);

CREATE TABLE car_owner
(
    human_id INTEGER REFERENCES human (id),
    car_id   INTEGER REFERENCES car (id)
);


SELECT s.name, age, f.name AS faculty
FROM student s
         LEFT JOIN faculty f on s.faculty_id = f.id;
SELECT s.name, s.age
FROM student s
         JOIN avatar a on s.id = a.student_id;