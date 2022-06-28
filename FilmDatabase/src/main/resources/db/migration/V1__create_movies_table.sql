CREATE SCHEMA IF NOT EXISTS filmdatabase;

CREATE TABLE movies (movie_id INTEGER NOT NULL AUTO_INCREMENT, author VARCHAR(255), title VARCHAR(255),
                    cost_to_rent INTEGER, PRIMARY KEY (movie_id));

INSERT INTO movies (author, title, cost_to_rent) values ('Christopher Nolan', 'Prestige', 600);
INSERT INTO movies (author, title, cost_to_rent) values ('Christopher Nolan', 'Interstellar', 600);
INSERT INTO movies (author, title, cost_to_rent) values ('Martin Scorsese', 'The Departed', 600);

CREATE TABLE deleted_movies (deleted_movie_id INTEGER NOT NULL AUTO_INCREMENT, author VARCHAR(255), movie_id INTEGER,
title VARCHAR(255), cost_to_rent INTEGER, time_of_delete DATETIME(6), PRIMARY KEY (deleted_movie_id))