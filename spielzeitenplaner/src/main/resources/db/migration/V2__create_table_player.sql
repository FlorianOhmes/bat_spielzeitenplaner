CREATE TABLE IF NOT EXISTS player (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    position VARCHAR(20),
    jersey_number INT
);
