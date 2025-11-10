CREATE TABLE posts (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    title     VARCHAR(255),
    content   TEXT,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);