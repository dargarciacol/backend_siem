package com.nova.nova_siem_backend.siem.incidents.dto;

import com.nova.nova_siem_backend.siem.alerts.model.AlertSeverity;
import com.nova.nova_siem_backend.siem.incidents.model.IncidentStatus;
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
public class IncidentResponseDTO {

    private UUID id;
    private String title;
    private String description;
    private AlertSeverity severity;
    private IncidentStatus status;
    private String assignedTo;
    private String primaryHypothesis;
    private Integer confidenceLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}