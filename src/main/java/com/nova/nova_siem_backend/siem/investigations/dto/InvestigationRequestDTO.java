package com.nova.nova_siem_backend.siem.investigations.dto;

import com.nova.nova_siem_backend.siem.investigations.model.InvestigationVerdict;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationRequestDTO {

    private InvestigationVerdict verdict;

    @Min(0)
    @Max(100)
    private Integer confidenceScore;

    private String recommendedAction;

    private List<String> factsNotebook;

    private List<String> inferencesNotebook;

    private List<String> pendingNotebook;
}