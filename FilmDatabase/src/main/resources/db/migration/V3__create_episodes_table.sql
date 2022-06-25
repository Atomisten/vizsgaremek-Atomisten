
CREATE TABLE episodes (episode_id INTEGER NOT NULL auto_increment, director VARCHAR(255), title VARCHAR(255),
                        series_id INTEGER, PRIMARY KEY (episode_id));

ALTER TABLE episodes ADD CONSTRAINT fk_series_episode FOREIGN KEY (series_id) REFERENCES series (series_id);

INSERT INTO episodes (title, director, series_id) values ('The North Remembers', 'Alan Taylor', 1);
INSERT INTO episodes (title, director, series_id) values ('What Is Dead May Never Die', 'Alik Sakharov', 1);

INSERT INTO episodes (title, director, series_id) values ('Johan Renck', 'Not Great, Not Terrible', 2);
INSERT INTO episodes (title, director, series_id) values ('Johan Renck', 'Please Remain Calm', 2);


-- David Benioff D.B. Weiss


CREATE TABLE deleted_episodes (deleted_episode_id INTEGER NOT NULL auto_increment, director VARCHAR(255),
                        episode_id INTEGER, time_of_delete datetime(6), title VARCHAR(255),
                        deleted_series_id INTEGER NOT NULL, PRIMARY KEY (deleted_episode_id));

ALTER TABLE deleted_episodes ADD CONSTRAINT fk_deleted_series_episode
    FOREIGN KEY (deleted_series_id) REFERENCES deleted_series (deleted_series_id);