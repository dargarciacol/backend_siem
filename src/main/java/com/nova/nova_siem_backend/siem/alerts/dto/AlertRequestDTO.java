package com.nova.nova_siem_backend.siem.alerts.dto;

import com.nova.nova_siem_backend.siem.alerts.model.AlertSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertRequestDTO {

    @NotBlank
    private String ruleTriggered;

    @NotNull
    private AlertSeverity severity;

    @NotNull
    private UUID eventId;
}