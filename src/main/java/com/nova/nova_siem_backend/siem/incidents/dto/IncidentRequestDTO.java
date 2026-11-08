package com.nova.nova_siem_backend.siem.incidents.dto;

import com.nova.nova_siem_backend.siem.alerts.model.AlertSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentRequestDTO {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private AlertSeverity severity;

    private String primaryHypothesis;

    private Integer confidenceLevel;
}