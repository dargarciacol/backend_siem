package com.nova.nova_siem_backend.siem.investigations.repository;

import com.nova.nova_siem_backend.siem.investigations.model.InvestigationEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvidenceRepository extends JpaRepository<InvestigationEvidence, UUID> {
    List<InvestigationEvidence> findByInvestigationId(UUID investigationId);
}
