package com.nova.nova_siem_backend.siem.detection.model;

import com.nova.nova_siem_backend.siem.alerts.model.AlertSeverity;
import com.nova.nova_siem_backend.siem.common.audit.AuditEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "detection_rules")
public class DetectionRule extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private RuleCategory category;

    @Column(name = "mitre_technique_id")
    private String mitreTechniqueId;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private AlertSeverity severity;

    @Column(name = "sigma_logic", columnDefinition = "TEXT", nullable = false)
    private String sigmaLogic;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "hit_count")
    private Long hitCount = 0L;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RuleCategory getCategory() {
        return category;
    }

    public void setCategory(RuleCategory category) {
        this.category = category;
    }

    public String getMitreTechniqueId() {
        return mitreTechniqueId;
    }

    public void setMitreTechniqueId(String mitreTechniqueId) {
        this.mitreTechniqueId = mitreTechniqueId;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AlertSeverity severity) {
        this.severity = severity;
    }

    public String getSigmaLogic() {
        return sigmaLogic;
    }

    public void setSigmaLogic(String sigmaLogic) {
        this.sigmaLogic = sigmaLogic;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getHitCount() {
        return hitCount;
    }

    public void setHitCount(Long hitCount) {
        this.hitCount = hitCount;
    }
}