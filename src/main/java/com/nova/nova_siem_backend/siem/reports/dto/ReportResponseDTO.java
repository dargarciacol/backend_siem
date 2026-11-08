package com.nova.nova_siem_backend.siem.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDTO {

    private String reportId;
    private String title;
    private String type;
    private String format;
    private String author;
    private LocalDateTime generatedAt;
    private Object data;
}