package com.nova.nova_siem_backend.siem.alerts.dto;

import com.nova.nova_siem_backend.siem.alerts.model.AlertSeverity;
import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus;
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
public class AlertResponseDTO {

    private UUID id;
    private String ruleTriggered;
    private AlertSeverity severity;
    private AlertStatus status;
    private String assignedTo;
    private UUID eventId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}