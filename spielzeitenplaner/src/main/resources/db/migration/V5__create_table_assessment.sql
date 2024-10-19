CREATE TABLE IF NOT EXISTS assessment (
    id SERIAL PRIMARY KEY,
    date DATE,
    criterion_id INT,
    player_id INT,
    value DOUBLE PRECISION
);
