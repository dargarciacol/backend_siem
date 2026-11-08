-- Asocia alertas a incidentes y eventos a alertas. Los tipos son UUID (igual que security_incidents.id y security_alerts.id).
ALTER TABLE security_alerts ADD COLUMN IF NOT EXISTS incident_id UUID;
ALTER TABLE security_events ADD COLUMN IF NOT EXISTS alert_id UUID;

DO $$ BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_alert_incident') THEN
    ALTER TABLE security_alerts ADD CONSTRAINT fk_alert_incident FOREIGN KEY (incident_id) REFERENCES security_incidents(id);
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_event_alert') THEN
    ALTER TABLE security_events ADD CONSTRAINT fk_event_alert FOREIGN KEY (alert_id) REFERENCES security_alerts(id);
  END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_alerts_incident ON security_alerts(incident_id);
CREATE INDEX IF NOT EXISTS idx_events_alert ON security_events(alert_id);
