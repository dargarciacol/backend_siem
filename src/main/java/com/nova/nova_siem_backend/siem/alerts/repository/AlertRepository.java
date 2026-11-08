package com.nova.nova_siem_backend.siem.alerts.repository;

import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus; // Asegúrate de importar el Enum
import com.nova.nova_siem_backend.siem.alerts.model.SecurityAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<SecurityAlert, UUID> {

    boolean existsBySourceEvent_IdAndRuleTriggered(
            UUID sourceEventId,
            String ruleTriggered
    );

    Optional<SecurityAlert> findByCorrelationKey(String correlationKey);

    boolean existsByCorrelationKey(String correlationKey);

    // NUEVO MÉTODO PARA EL DASHBOARD
    long countByStatusIn(Collection<AlertStatus> statuses);
}