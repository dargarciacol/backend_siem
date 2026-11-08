package com.nova.nova_siem_backend.siem.events.controller;

import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import com.nova.nova_siem_backend.siem.events.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SecurityEvent>>> getAllEvents(
            @PageableDefault(size = 200, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SecurityEvent> events = eventService.getAllEvents(pageable);
        return ResponseEntity.ok(ApiResponse.success("Events retrieved successfully", events));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SecurityEvent>> getEventById(@PathVariable UUID id) {
        SecurityEvent event = eventService.getEventById(id);
        return ResponseEntity.ok(ApiResponse.success("Event retrieved successfully", event));
    }
}