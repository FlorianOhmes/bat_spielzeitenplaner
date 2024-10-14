CREATE TABLE IF NOT EXISTS formation (
    id SERIAL PRIMARY KEY,
    name VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS position (
    id SERIAL PRIMARY KEY,
    formation INTEGER REFERENCES formation(id), 
    formation_key INTEGER,
    name VARCHAR(5)
);
