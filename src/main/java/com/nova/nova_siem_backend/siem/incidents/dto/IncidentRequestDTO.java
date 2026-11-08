package com.nova.nova_siem_backend.siem.incidents.dto;

import com.nova.nova_siem_backend.siem.alerts.model.AlertSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

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

    // Opcional: alertas existentes a asociar al incidente.
    private List<UUID> alertIds;
}