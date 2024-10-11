-- CREATE TABLE resume (
--     resume_id INT AUTO_INCREMENT PRIMARY KEY,
--     user_id INT NOT NULL UNIQUE,
--     resume_file_path VARCHAR(255) NOT NULL,
--     FOREIGN KEY (user_id) REFERENCES user(user_id)
--         ON DELETE CASCADE
-- );


CREATE TABLE resume (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Corresponds to Long id in Java, auto-incrementing
    user_id BIGINT NOT NULL,               -- Corresponds to Long userId in Java, required
    file_path VARCHAR(255) NOT NULL        -- Corresponds to String filePath in Java, required
);

ALTER TABLE resume DROP COLUMN file_path;