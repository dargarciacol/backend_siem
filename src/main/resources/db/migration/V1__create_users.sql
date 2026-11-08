CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       is_active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP,
                       created_by VARCHAR(50),
                       updated_by VARCHAR(50)
);

INSERT INTO users (username, password, full_name, role)
VALUES ('s.garcia', '$2a$10$xyz...', 'Santiago Garcia', 'SOC_L2');