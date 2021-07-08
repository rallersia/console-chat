CREATE TABLE users (
                         identifier SERIAL PRIMARY KEY,
                         username varchar(25) UNIQUE,
                         password varchar(60)
);