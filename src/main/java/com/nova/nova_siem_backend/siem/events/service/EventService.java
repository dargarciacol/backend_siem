package com.nova.nova_siem_backend.siem.events.service;

import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import com.nova.nova_siem_backend.siem.events.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Page<SecurityEvent> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public SecurityEvent getEventById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));
    }
}