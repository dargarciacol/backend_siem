package com.nova.nova_siem_backend.siem.events.mapper;

import com.nova.nova_siem_backend.siem.events.dto.EventRequestDTO;
import com.nova.nova_siem_backend.siem.events.dto.EventResponseDTO;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    public SecurityEvent toEntity(EventRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        SecurityEvent event = new SecurityEvent();
        event.setTimestamp(LocalDateTime.now());
        event.setSourceIp(dto.getSourceIp());
        event.setUserIdentity(dto.getUserIdentity());
        event.setEventType(dto.getEventType());
        event.setAction(dto.getAction());
        event.setSeverity(dto.getSeverity());
        event.setRawData(dto.getRawData());
        event.setSynthetic(dto.getSynthetic() != null ? dto.getSynthetic() : false);

        return event;
    }

    public EventResponseDTO toDto(SecurityEvent entity) {
        if (entity == null) {
            return null;
        }

        return EventResponseDTO.builder()
                .id(entity.getId())
                .timestamp(entity.getTimestamp())
                .sourceIp(entity.getSourceIp())
                .userIdentity(entity.getUserIdentity())
                .eventType(entity.getEventType())
                .action(entity.getAction())
                .severity(entity.getSeverity())
                .rawData(entity.getRawData())
                .synthetic(entity.isSynthetic())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}