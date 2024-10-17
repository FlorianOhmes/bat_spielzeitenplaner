CREATE TABLE IF NOT EXISTS criterion (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    abbrev VARCHAR(2),
    weight DOUBLE PRECISION
);
