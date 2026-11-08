-- Reemplazada por no-op: la tabla indicators_of_compromise real (UUID, investigation_id, ioc_type,
-- is_malicious, threat_context) se crea en V8, acorde a la entidad IOC. La version anterior creaba
-- una tabla BIGSERIAL incompatible que, por CREATE IF NOT EXISTS, habria impedido crear la correcta.
SELECT 1;
