package com.nova.nova_siem_backend.siem.investigations.model;

import com.nova.nova_siem_backend.siem.common.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "investigation_evidences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestigationEvidence extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investigation_id", nullable = false)
    private Investigation investigation;

    @Column(name = "evidence_type", nullable = false)
    private String evidenceType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "artifact_reference")
    private String artifactReference;
}