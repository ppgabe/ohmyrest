CREATE TABLE posts
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255),
    content    TEXT,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE users
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(32) UNIQUE,
    full_name       VARCHAR(255),
    email           VARCHAR(64) UNIQUE,
    hashed_password VARCHAR(255),
    created_at      TIMESTAMP WITH TIME ZONE
);

ALTER TABLE posts
    ADD COLUMN author_id INT;

ALTER TABLE posts
    ADD CONSTRAINT post_author_id FOREIGN KEY (author_id) REFERENCES users (id);