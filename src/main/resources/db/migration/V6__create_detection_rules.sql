CREATE TABLE detection_rules (
                                 id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                 name VARCHAR(255) NOT NULL UNIQUE,
                                 category VARCHAR(100) NOT NULL,
                                 mitre_technique_id VARCHAR(50),
                                 severity VARCHAR(50) NOT NULL,
                                 sigma_logic TEXT NOT NULL,
                                 is_active BOOLEAN NOT NULL DEFAULT TRUE,
                                 hit_count BIGINT DEFAULT 0,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP,
                                 created_by VARCHAR(50),
                                 updated_by VARCHAR(50)
);

CREATE INDEX idx_det_rules_active ON detection_rules(is_active);
CREATE INDEX idx_det_rules_mitre ON detection_rules(mitre_technique_id);