CREATE DATABASE IF NOT EXISTS collections_project
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE collections_project;

CREATE TABLE IF NOT EXISTS video_source (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    episode_number INT NOT NULL,
    line_name VARCHAR(100) NOT NULL,
    play_type VARCHAR(10) NOT NULL,
    play_url VARCHAR(2048) NOT NULL
);
