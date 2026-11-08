package com.nova.nova_siem_backend.siem.ingestion.service;

import com.nova.nova_siem_backend.siem.events.model.EventSeverity;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class EventNormalizationService {

    public SecurityEvent normalizeEvent(String sourceIp, String userIdentity, String eventType,
                                        String action, String severityStr, Map<String, Object> rawData) {

        SecurityEvent event = new SecurityEvent();
        event.setTimestamp(LocalDateTime.now());
        event.setSourceIp(sourceIp != null && !sourceIp.trim().isEmpty() ? sourceIp.trim() : "0.0.0.0");
        event.setUserIdentity(userIdentity != null && !userIdentity.trim().isEmpty() ? userIdentity.trim() : "UNKNOWN");
        event.setEventType(eventType != null && !eventType.trim().isEmpty() ? eventType.trim() : "Generic_Event");
        event.setAction(action != null ? action.trim() : "None");

        try {
            event.setSeverity(severityStr != null && !severityStr.trim().isEmpty() ?
                    EventSeverity.valueOf(severityStr.trim().toUpperCase()) : EventSeverity.INFO);
        } catch (IllegalArgumentException e) {
            event.setSeverity(EventSeverity.INFO);
        }

        event.setRawData(rawData);
        event.setSynthetic(true);

        return event;
    }
}