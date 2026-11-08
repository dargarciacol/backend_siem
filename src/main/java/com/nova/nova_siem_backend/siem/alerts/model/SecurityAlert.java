
package com.nova.nova_siem_backend.siem.alerts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.nova.nova_siem_backend.siem.common.audit.AuditEntity;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "security_alerts")
public class SecurityAlert extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rule_triggered", nullable = false)
    private String ruleTriggered;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private AlertSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AlertStatus status;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "correlation_key", unique = true, length = 255)
    private String correlationKey;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private SecurityEvent sourceEvent;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRuleTriggered() {
        return ruleTriggered;
    }

    public void setRuleTriggered(String ruleTriggered) {
        this.ruleTriggered = ruleTriggered;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AlertSeverity severity) {
        this.severity = severity;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getCorrelationKey() {
        return correlationKey;
    }

    public void setCorrelationKey(String correlationKey) {
        this.correlationKey = correlationKey;
    }

    public SecurityEvent getSourceEvent() {
        return sourceEvent;
    }

    public void setSourceEvent(SecurityEvent sourceEvent) {
        this.sourceEvent = sourceEvent;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id")
    private SecurityIncident incident;

    @JsonIgnore
    @OneToMany(mappedBy = "alert", fetch = FetchType.LAZY)
    private List<SecurityEvent> events = new ArrayList<>();

    public SecurityIncident getIncident() {
        return incident;
    }

    public void setIncident(SecurityIncident incident) {
        this.incident = incident;
    }

    public List<SecurityEvent> getEvents() {
        return events;
    }

    public void setEvents(List<SecurityEvent> events) {
        this.events = events;
    }
}
