package com.nova.nova_siem_backend.siem.investigations.model;

import com.nova.nova_siem_backend.siem.common.audit.AuditEntity;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "investigations")
public class Investigation extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false, unique = true)
    private SecurityIncident incident;

    @Enumerated(EnumType.STRING)
    @Column(name = "verdict")
    private InvestigationVerdict verdict;

    @Column(name = "confidence_score")
    private Integer confidenceScore;

    @Column(name = "recommended_action")
    private String recommendedAction;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "facts_notebook", columnDefinition = "jsonb")
    private List<String> factsNotebook;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "inferences_notebook", columnDefinition = "jsonb")
    private List<String> inferencesNotebook;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "pending_notebook", columnDefinition = "jsonb")
    private List<String> pendingNotebook;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SecurityIncident getIncident() {
        return incident;
    }

    public void setIncident(SecurityIncident incident) {
        this.incident = incident;
    }

    public InvestigationVerdict getVerdict() {
        return verdict;
    }

    public void setVerdict(InvestigationVerdict verdict) {
        this.verdict = verdict;
    }

    public Integer getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Integer confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }

    public List<String> getFactsNotebook() {
        return factsNotebook;
    }

    public void setFactsNotebook(List<String> factsNotebook) {
        this.factsNotebook = factsNotebook;
    }

    public List<String> getInferencesNotebook() {
        return inferencesNotebook;
    }

    public void setInferencesNotebook(List<String> inferencesNotebook) {
        this.inferencesNotebook = inferencesNotebook;
    }

    public List<String> getPendingNotebook() {
        return pendingNotebook;
    }

    public void setPendingNotebook(List<String> pendingNotebook) {
        this.pendingNotebook = pendingNotebook;
    }
}