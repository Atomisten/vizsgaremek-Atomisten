CREATE SCHEMA IF NOT EXISTS filmdatabase;

CREATE TABLE movies (movie_id INTEGER NOT NULL AUTO_INCREMENT,
                        author VARCHAR(255), title VARCHAR(255), PRIMARY KEY (movie_id));

INSERT INTO movies (author, title) values ('Christopher Nolan', 'Prestige');
INSERT INTO movies (author, title) values ('Christopher Nolan', 'Interstellar');
INSERT INTO movies (author, title) values ('Martin Scorsese', 'The Departed');

create table deleted_movies (deleted_movie_id INTEGER NOT NULL AUTO_INCREMENT,
                        author VARCHAR(255), movie_id INTEGER, title VARCHAR(255), PRIMARY KEY (deleted_movie_id))