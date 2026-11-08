-- NOTE: Flyway is currently disabled (spring.flyway.enabled=false) and the schema is
-- maintained by Hibernate (ddl-auto=update). This migration documents and idempotently
-- reproduces the objects the JPA entities need that V1-V6 never created, so that
-- Flyway can be enabled (and ddl-auto switched to "validate") in the future.

ALTER TABLE security_alerts
    ADD COLUMN IF NOT EXISTS correlation_key VARCHAR(255);

CREATE UNIQUE INDEX IF NOT EXISTS uq_sec_alerts_correlation_key
    ON security_alerts(correlation_key);

CREATE TABLE IF NOT EXISTS indicators_of_compromise (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    investigation_id UUID NOT NULL,
    ioc_type VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    is_malicious BOOLEAN NOT NULL DEFAULT FALSE,
    threat_context TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    CONSTRAINT fk_ioc_investigation FOREIGN KEY (investigation_id)
        REFERENCES investigations(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_ioc_investigation ON indicators_of_compromise(investigation_id);
CREATE INDEX IF NOT EXISTS idx_ioc_value ON indicators_of_compromise(value);

CREATE TABLE IF NOT EXISTS investigation_evidences (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    investigation_id UUID NOT NULL,
    evidence_type VARCHAR(255) NOT NULL,
    description TEXT,
    artifact_reference VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    CONSTRAINT fk_evidence_investigation FOREIGN KEY (investigation_id)
        REFERENCES investigations(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_evidence_investigation ON investigation_evidences(investigation_id);

-- Indexes used by the detection engine / dashboard queries
CREATE INDEX IF NOT EXISTS idx_sec_events_type_action ON security_events(event_type, action);
CREATE INDEX IF NOT EXISTS idx_sec_alerts_created_at ON security_alerts(created_at);
