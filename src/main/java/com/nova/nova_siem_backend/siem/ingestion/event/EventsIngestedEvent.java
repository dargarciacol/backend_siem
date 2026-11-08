package com.nova.nova_siem_backend.siem.ingestion.event;

import java.util.List;
import java.util.UUID;

public record EventsIngestedEvent(List<UUID> eventIds) {

    public EventsIngestedEvent {
        eventIds = List.copyOf(eventIds);
    }
}