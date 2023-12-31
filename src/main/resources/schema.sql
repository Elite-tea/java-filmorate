DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS genre CASCADE;
DROP TABLE IF EXISTS film_genre CASCADE;
DROP TABLE IF EXISTS feed CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS film_directors CASCADE;

CREATE TABLE IF NOT EXISTS mpa (
    mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpa_name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film (
    film_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR(200),
    release_date DATE,
    duration INTEGER CHECK (duration > 0),
    mpa_id INTEGER REFERENCES mpa (mpa_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id BIGINT  NOT NULL REFERENCES film (film_id) ON DELETE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES genre (genre_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR NOT NULL UNIQUE,
    login VARCHAR NOT NULL UNIQUE,
    name VARCHAR NOT NULL,
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT  NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    friend_id BIGINT  NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    status BOOLEAN NOT NULL,
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL REFERENCES film (film_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content VARCHAR NOT NULL,
    is_positive BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    film_id BIGINT NOT NULL REFERENCES film (film_id) ON DELETE CASCADE,
    useful INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS film_reviews (
    review_id INTEGER NOT NULL REFERENCES reviews (review_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    is_positive BOOLEAN NOT NULL,
    PRIMARY KEY (review_id, user_id)
);

CREATE TABLE IF NOT EXISTS directors (
    director_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    director_name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_directors (
    director_id INTEGER REFERENCES directors (director_id) ON DELETE CASCADE,
    film_id BIGINT REFERENCES film (film_id) ON DELETE CASCADE,
    PRIMARY KEY (director_id, film_id)
);

CREATE TABLE IF NOT EXISTS feed (
    time TIMESTAMP NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    event_type VARCHAR NOT NULL,
    operation VARCHAR NOT NULL,
    event_id INTEGER NOT NULL AUTO_INCREMENT,
    entity_id INTEGER NOT NULL,
    CONSTRAINT event_id_pk PRIMARY KEY (event_id)
);