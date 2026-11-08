package com.nova.nova_siem_backend.siem.investigations.dto;

import com.nova.nova_siem_backend.siem.investigations.model.InvestigationVerdict;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationResponseDTO {

    private UUID id;
    private UUID incidentId;
    private InvestigationVerdict verdict;
    private Integer confidenceScore;
    private String recommendedAction;
    private List<String> factsNotebook;
    private List<String> inferencesNotebook;
    private List<String> pendingNotebook;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}