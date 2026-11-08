package com.nova.nova_siem_backend.siem.detection.dto;

import com.nova.nova_siem_backend.siem.alerts.model.AlertSeverity;
import com.nova.nova_siem_backend.siem.detection.model.RuleCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectionRuleResponseDTO {

    private UUID id;
    private String name;
    private RuleCategory category;
    private String mitreTechniqueId;
    private AlertSeverity severity;
    private String sigmaLogic;
    private boolean active;
    private Long hitCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}