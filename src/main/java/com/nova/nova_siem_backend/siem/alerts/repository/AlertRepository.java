package com.nova.nova_siem_backend.siem.alerts.repository;

import com.nova.nova_siem_backend.siem.alerts.model.SecurityAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<SecurityAlert, UUID> {

    boolean existsBySourceEvent_IdAndRuleTriggered(
            UUID sourceEventId,
            String ruleTriggered
    );
}