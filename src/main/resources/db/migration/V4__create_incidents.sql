CREATE TABLE security_incidents (
                                    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                    title VARCHAR(255) NOT NULL,
                                    description TEXT,
                                    severity VARCHAR(50) NOT NULL,
                                    status VARCHAR(50) NOT NULL,
                                    assigned_to VARCHAR(50),
                                    primary_hypothesis VARCHAR(255),
                                    confidence_level INTEGER,
                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP,
                                    created_by VARCHAR(50),
                                    updated_by VARCHAR(50)
);

CREATE INDEX idx_sec_incidents_status ON security_incidents(status);
CREATE INDEX idx_sec_incidents_severity ON security_incidents(severity);