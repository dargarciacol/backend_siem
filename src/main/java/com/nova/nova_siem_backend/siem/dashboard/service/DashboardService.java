package com.nova.nova_siem_backend.siem.dashboard.service;

import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus;
import com.nova.nova_siem_backend.siem.alerts.repository.AlertRepository;
import com.nova.nova_siem_backend.siem.dashboard.dto.DashboardSummaryDTO;
import com.nova.nova_siem_backend.siem.events.repository.EventRepository;
import com.nova.nova_siem_backend.siem.incidents.model.IncidentStatus;
import com.nova.nova_siem_backend.siem.incidents.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DashboardService {

    private final EventRepository eventRepository;
    private final AlertRepository alertRepository;
    private final IncidentRepository incidentRepository;

    public DashboardService(EventRepository eventRepository, AlertRepository alertRepository, IncidentRepository incidentRepository) {
        this.eventRepository = eventRepository;
        this.alertRepository = alertRepository;
        this.incidentRepository = incidentRepository;
    }

    public DashboardSummaryDTO getSummaryMetrics() {
        long totalEvents = eventRepository.count();

        long openAlerts = alertRepository.findAll().stream()
                .filter(a -> a.getStatus() == AlertStatus.NEW || a.getStatus() == AlertStatus.IN_PROGRESS)
                .count();

        long activeIncidents = incidentRepository.findAll().stream()
                .filter(i -> i.getStatus() != IncidentStatus.CLOSED)
                .count();

        long criticalIncidents = incidentRepository.findAll().stream()
                .filter(i -> i.getStatus() != IncidentStatus.CLOSED && i.getSeverity().name().equals("CRITICAL"))
                .count();

        return new DashboardSummaryDTO(totalEvents, activeIncidents, openAlerts, criticalIncidents);
    }
}