package com.nova.nova_siem_backend.siem.investigations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimelineDTO {

    private LocalDateTime timestamp;
    private String category;
    private String title;
    private String description;
    private String source;
}