CREATE TABLE security_events (
                                 id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                 timestamp TIMESTAMP NOT NULL,
                                 source_ip VARCHAR(45),
                                 user_identity VARCHAR(255),
                                 event_type VARCHAR(100) NOT NULL,
                                 action VARCHAR(255),
                                 severity VARCHAR(50) NOT NULL,
                                 raw_data JSONB,
                                 is_synthetic BOOLEAN NOT NULL DEFAULT FALSE,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP,
                                 created_by VARCHAR(50),
                                 updated_by VARCHAR(50)
);

CREATE INDEX idx_sec_events_timestamp ON security_events(timestamp);
CREATE INDEX idx_sec_events_source_ip ON security_events(source_ip);
CREATE INDEX idx_sec_events_severity ON security_events(severity);