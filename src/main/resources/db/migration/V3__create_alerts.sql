CREATE TABLE security_alerts (
                                 id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                 rule_triggered VARCHAR(255) NOT NULL,
                                 severity VARCHAR(50) NOT NULL,
                                 status VARCHAR(50) NOT NULL,
                                 assigned_to VARCHAR(50),
                                 event_id UUID NOT NULL,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP,
                                 created_by VARCHAR(50),
                                 updated_by VARCHAR(50),
                                 CONSTRAINT fk_alert_event FOREIGN KEY (event_id) REFERENCES security_events(id) ON DELETE CASCADE
);

CREATE INDEX idx_sec_alerts_status ON security_alerts(status);
CREATE INDEX idx_sec_alerts_severity ON security_alerts(severity);