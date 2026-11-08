package com.nova.nova_siem_backend.siem.investigations.mapper;

import com.nova.nova_siem_backend.siem.investigations.dto.InvestigationResponseDTO;
import com.nova.nova_siem_backend.siem.investigations.model.Investigation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class InvestigationMapper {

    public InvestigationResponseDTO toDto(Investigation entity) {
        if (entity == null) {
            return null;
        }

        return InvestigationResponseDTO.builder()
                .id(entity.getId())
                .incidentId(entity.getIncident() != null ? entity.getIncident().getId() : null)
                .verdict(entity.getVerdict())
                .confidenceScore(entity.getConfidenceScore())
                .recommendedAction(entity.getRecommendedAction())
                .factsNotebook(entity.getFactsNotebook() != null ? new ArrayList<>(entity.getFactsNotebook()) : new ArrayList<>())
                .inferencesNotebook(entity.getInferencesNotebook() != null ? new ArrayList<>(entity.getInferencesNotebook()) : new ArrayList<>())
                .pendingNotebook(entity.getPendingNotebook() != null ? new ArrayList<>(entity.getPendingNotebook()) : new ArrayList<>())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}