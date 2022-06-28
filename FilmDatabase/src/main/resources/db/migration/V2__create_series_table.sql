
CREATE TABLE series (series_id INTEGER NOT NULL auto_increment, author VARCHAR(255),
                    title VARCHAR(255), PRIMARY KEY (series_id));

INSERT INTO series (author, title) VALUES ('George R. R. Martin', 'Game of Thrones');
INSERT INTO series (author, title) VALUES ('Craig Mazin', 'Chernobyl');


CREATE TABLE deleted_series (deleted_series_id INTEGER NOT NULL auto_increment, author VARCHAR(255),
time_of_delete DATETIME(6), series_id INTEGER, title VARCHAR(255), PRIMARY KEY (deleted_series_id));