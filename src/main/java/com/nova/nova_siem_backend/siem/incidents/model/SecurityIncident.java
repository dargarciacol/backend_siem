package com.nova.nova_siem_backend.siem.incidents.model;

import com.nova.nova_siem_backend.siem.alerts.model.AlertSeverity;
import com.nova.nova_siem_backend.siem.common.audit.AuditEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "security_incidents")
public class SecurityIncident extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private AlertSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private IncidentStatus status;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "primary_hypothesis")
    private String primaryHypothesis;

    @Column(name = "confidence_level")
    private Integer confidenceLevel;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AlertSeverity severity) {
        this.severity = severity;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getPrimaryHypothesis() {
        return primaryHypothesis;
    }

    public void setPrimaryHypothesis(String primaryHypothesis) {
        this.primaryHypothesis = primaryHypothesis;
    }

    public Integer getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Integer confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
}