package com.nova.nova_siem_backend.siem.detection.dto;

import com.nova.nova_siem_backend.siem.alerts.model.AlertSeverity;
import com.nova.nova_siem_backend.siem.detection.model.RuleCategory;
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
public class DetectionRuleRequestDTO {

    @NotBlank
    private String name;

    @NotNull
    private RuleCategory category;

    private String mitreTechniqueId;

    @NotNull
    private AlertSeverity severity;

    @NotBlank
    private String sigmaLogic;

    @NotNull
    private Boolean active;
}