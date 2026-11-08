CREATE TABLE investigations (
                                id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                incident_id UUID NOT NULL UNIQUE,
                                verdict VARCHAR(50),
                                confidence_score INTEGER,
                                recommended_action VARCHAR(255),
                                facts_notebook JSONB,
                                inferences_notebook JSONB,
                                pending_notebook JSONB,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP,
                                created_by VARCHAR(50),
                                updated_by VARCHAR(50),
                                CONSTRAINT fk_investigation_incident FOREIGN KEY (incident_id) REFERENCES security_incidents(id) ON DELETE CASCADE
);