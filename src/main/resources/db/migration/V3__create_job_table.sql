
CREATE TABLE job (
     job_id INT AUTO_INCREMENT PRIMARY KEY,
     job_title VARCHAR(255) DEFAULT 'Software Engineer Intern' NOT NULL,
     company VARCHAR(255) NOT NULL,
     job_url VARCHAR(255) DEFAULT NULL
);