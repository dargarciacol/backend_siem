package com.nova.nova_siem_backend.siem.dashboard.service;

import com.nova.nova_siem_backend.siem.alerts.model.AlertSeverity;
import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus;
import com.nova.nova_siem_backend.siem.alerts.repository.AlertRepository;
import com.nova.nova_siem_backend.siem.dashboard.dto.DashboardSummaryDTO;
import com.nova.nova_siem_backend.siem.detection.repository.DetectionRuleRepository;
import com.nova.nova_siem_backend.siem.events.repository.EventRepository;
import com.nova.nova_siem_backend.siem.incidents.model.IncidentStatus;
import com.nova.nova_siem_backend.siem.incidents.repository.IncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DashboardService {

    // Alerts that still require analyst attention.
    private static final List<AlertStatus> OPEN_ALERT_STATUSES = List.of(
            AlertStatus.NEW,
            AlertStatus.IN_PROGRESS,
            AlertStatus.UNDER_INVESTIGATION,
            AlertStatus.ESCALATED
    );

    private final EventRepository eventRepository;
    private final AlertRepository alertRepository;
    private final IncidentRepository incidentRepository;
    private final DetectionRuleRepository ruleRepository;

    public DashboardService(
            EventRepository eventRepository,
            AlertRepository alertRepository,
            IncidentRepository incidentRepository,
            DetectionRuleRepository ruleRepository) {
        this.eventRepository = eventRepository;
        this.alertRepository = alertRepository;
        this.incidentRepository = incidentRepository;
        this.ruleRepository = ruleRepository;
    }

    /**
     * Global metrics computed with COUNT queries in the database
     * (previously every alert and incident was loaded into memory).
     */
    @Transactional(readOnly = true)
    public DashboardSummaryDTO getSummaryMetrics() {
        long totalEvents = eventRepository.count();
        long openAlerts = alertRepository.countByStatusIn(OPEN_ALERT_STATUSES);
        long activeIncidents = incidentRepository.countByStatusNot(IncidentStatus.CLOSED);
        long criticalIncidents = incidentRepository.countByStatusNotAndSeverity(
                IncidentStatus.CLOSED, AlertSeverity.CRITICAL);
        long activeRules = ruleRepository.countByActiveTrue();

        return new DashboardSummaryDTO(
                totalEvents, activeIncidents, openAlerts, criticalIncidents, activeRules);
    }
}
