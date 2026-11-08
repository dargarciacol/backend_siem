CREATE TABLE IF NOT EXISTS indicators_of_compromise (
                                                        id BIGSERIAL PRIMARY KEY,
                                                        type VARCHAR(50) NOT NULL, -- ej. IP, HASH, DOMAIN
    value VARCHAR(255) NOT NULL,
    description TEXT,
    severity VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );