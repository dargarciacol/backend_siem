package com.nova.nova_siem_backend.siem.investigations.repository;

import com.nova.nova_siem_backend.siem.investigations.model.Investigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvestigationRepository extends JpaRepository<Investigation, UUID> {
    Optional<Investigation> findByIncidentId(UUID incidentId);
}