package com.nova.nova_siem_backend.siem.investigations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EvidenceRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String evidenceType;

    @Size(max = 4000)
    private String description;

    @Size(max = 255)
    private String artifactReference;
}
