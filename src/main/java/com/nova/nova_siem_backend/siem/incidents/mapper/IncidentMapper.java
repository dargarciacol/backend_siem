package com.nova.nova_siem_backend.siem.incidents.mapper;

import com.nova.nova_siem_backend.siem.incidents.dto.IncidentRequestDTO;
import com.nova.nova_siem_backend.siem.incidents.dto.IncidentResponseDTO;
import com.nova.nova_siem_backend.siem.incidents.model.IncidentStatus;
import com.nova.nova_siem_backend.siem.incidents.model.SecurityIncident;
import org.springframework.stereotype.Component;

@Component
public class IncidentMapper {

    public SecurityIncident toEntity(IncidentRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        SecurityIncident incident = new SecurityIncident();
        incident.setTitle(dto.getTitle());
        incident.setDescription(dto.getDescription());
        incident.setSeverity(dto.getSeverity());
        incident.setPrimaryHypothesis(dto.getPrimaryHypothesis());
        incident.setConfidenceLevel(dto.getConfidenceLevel());
        incident.setStatus(IncidentStatus.TRIAGE);

        return incident;
    }

    public IncidentResponseDTO toDto(SecurityIncident entity) {
        if (entity == null) {
            return null;
        }

        return IncidentResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .severity(entity.getSeverity())
                .status(entity.getStatus())
                .assignedTo(entity.getAssignedTo())
                .primaryHypothesis(entity.getPrimaryHypothesis())
                .confidenceLevel(entity.getConfidenceLevel())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}