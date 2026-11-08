package com.nova.nova_siem_backend.siem.alerts.mapper;

import com.nova.nova_siem_backend.siem.alerts.dto.AlertRequestDTO;
import com.nova.nova_siem_backend.siem.alerts.dto.AlertResponseDTO;
import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus;
import com.nova.nova_siem_backend.siem.alerts.model.SecurityAlert;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {

    public SecurityAlert toEntity(AlertRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        SecurityAlert alert = new SecurityAlert();
        alert.setRuleTriggered(dto.getRuleTriggered());
        alert.setSeverity(dto.getSeverity());
        alert.setStatus(AlertStatus.NEW);

        return alert;
    }

    public AlertResponseDTO toDto(SecurityAlert entity) {
        if (entity == null) {
            return null;
        }

        return AlertResponseDTO.builder()
                .id(entity.getId())
                .ruleTriggered(entity.getRuleTriggered())
                .severity(entity.getSeverity())
                .status(entity.getStatus())
                .assignedTo(entity.getAssignedTo())
                .eventId(entity.getSourceEvent() != null ? entity.getSourceEvent().getId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}