package com.nova.nova_siem_backend.siem.ingestion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngestionResponseDTO {

    private String status;
    private String message;
    private int ingestedEventsCount;
    private String fileName;
}