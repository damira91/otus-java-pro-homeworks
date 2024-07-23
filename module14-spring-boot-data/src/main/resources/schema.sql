DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id     BIGSERIAL PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    email  VARCHAR(255) UNIQUE,
    phone VARCHAR(255)
);
INSERT INTO users (name, email, phone)
VALUES ('Vasia', 'vasia@example.com', '+71234567890'),
       ('Zenek', 'zenek@example.com', '+4876543210'),
       ('Boss', 'boss@example.com', '+1234567890');