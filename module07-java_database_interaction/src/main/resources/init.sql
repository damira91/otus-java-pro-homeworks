
CREATE TABLE if NOT EXISTS users (
                         id BIGSERIAL PRIMARY KEY,
                         login VARCHAR(255) NOT NULL,
                         password VARCHAR(255) NOT NULL,
                         nickname VARCHAR(255) NOT NULL
);

CREATE TABLE if NOT EXISTS accounts (
                         id BIGSERIAL PRIMARY KEY,
                         amount BIGINT NOT NULL,
                         tp VARCHAR(255) NOT NULL,
                         status VARCHAR(255)
);

