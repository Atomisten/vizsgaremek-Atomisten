CREATE SCHEMA IF NOT EXISTS filmdatabase;

CREATE TABLE movies (movie_id INTEGER NOT NULL AUTO_INCREMENT, author VARCHAR(255), title VARCHAR(255),
                    cost_to_rent INTEGER, PRIMARY KEY (movie_id));

INSERT INTO movies (title, author, cost_to_rent) values ('Prestige', 'Christopher Nolan', 600);
INSERT INTO movies (title, author, cost_to_rent) values ('Interstellar', 'Christopher Nolan',  600);
INSERT INTO movies (title, author, cost_to_rent) values ('The Departed', 'Martin Scorsese',  600);

CREATE TABLE deleted_movies (deleted_movie_id INTEGER NOT NULL AUTO_INCREMENT, author VARCHAR(255), movie_id INTEGER,
title VARCHAR(255), cost_to_rent INTEGER, time_of_delete DATETIME(6), PRIMARY KEY (deleted_movie_id))