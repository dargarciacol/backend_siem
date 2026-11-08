package com.nova.nova_siem_backend.siem.events.dto;

import com.nova.nova_siem_backend.siem.events.model.EventSeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {

    private UUID id;
    private LocalDateTime timestamp;
    private String sourceIp;
    private String userIdentity;
    private String eventType;
    private String action;
    private EventSeverity severity;
    private Map<String, Object> rawData;
    private boolean synthetic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}