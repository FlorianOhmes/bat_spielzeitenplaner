INSERT INTO criterion (name, abbrev, weight) VALUES
('Trainingsbeteiligung', 'T', 0.5), 
('Leistung', 'L', 0.5);

INSERT INTO formation (id, name) VALUES (9999, '3-4-3');

INSERT INTO position (formation, formation_key, name) VALUES
(9999, 0, 'TW'), 
(9999, 1, 'LIV'), 
(9999, 2, 'ZIV'), 
(9999, 3, 'RIV'), 
(9999, 4, 'LAV'), 
(9999, 5, 'LZM'), 
(9999, 6, 'RZM'), 
(9999, 7, 'RAV'), 
(9999, 8, 'LF'), 
(9999, 9, 'ST'), 
(9999, 10, 'RF');
