package com.nova.nova_siem_backend.siem.investigations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IocResponseDTO {
    private UUID id;
    private UUID investigationId;
    private String iocType;
    private String value;
    private boolean malicious;
    private String threatContext;
    private LocalDateTime createdAt;
}
