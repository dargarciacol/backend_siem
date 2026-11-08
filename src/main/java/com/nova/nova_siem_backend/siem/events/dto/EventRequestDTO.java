package com.nova.nova_siem_backend.siem.events.dto;

import com.nova.nova_siem_backend.siem.events.model.EventSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {

    private String sourceIp;

    private String userIdentity;

    @NotBlank
    private String eventType;

    private String action;

    @NotNull
    private EventSeverity severity;

    private Map<String, Object> rawData;

    @NotNull
    private Boolean synthetic;
}