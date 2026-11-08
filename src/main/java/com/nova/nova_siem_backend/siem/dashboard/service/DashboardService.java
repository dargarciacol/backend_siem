package com.nova.nova_siem_backend.siem.dashboard.service;

import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus;
import com.nova.nova_siem_backend.siem.alerts.repository.AlertRepository;
import com.nova.nova_siem_backend.siem.dashboard.dto.DashboardSummaryDTO;
import com.nova.nova_siem_backend.siem.detection.repository.DetectionRuleRepository; // NUEVO
import com.nova.nova_siem_backend.siem.events.repository.EventRepository;
import com.nova.nova_siem_backend.siem.incidents.model.IncidentStatus;
import com.nova.nova_siem_backend.siem.incidents.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DashboardService {

    private final EventRepository eventRepository;
    private final AlertRepository alertRepository;
    private final IncidentRepository incidentRepository;
    private final DetectionRuleRepository ruleRepository; // NUEVO

    public DashboardService(EventRepository eventRepository,
                            AlertRepository alertRepository,
                            IncidentRepository incidentRepository,
                            DetectionRuleRepository ruleRepository) { // NUEVO
        this.eventRepository = eventRepository;
        this.alertRepository = alertRepository;
        this.incidentRepository = incidentRepository;
        this.ruleRepository = ruleRepository; // NUEVO
    }

    public DashboardSummaryDTO getSummaryMetrics() {
        // 1. Eventos totales
        long totalEvents = eventRepository.count();

        // 2. Alertas activas (NEW o IN_PROGRESS) - Optimizado con consulta SQL a PostgreSQL
        List<AlertStatus> activeStatuses = Arrays.asList(AlertStatus.NEW, AlertStatus.IN_PROGRESS);
        long activeAlerts = alertRepository.countByStatusIn(activeStatuses);

        // 3. Incidentes abiertos (Diferente a CLOSED) - Optimizado
        long openIncidents = incidentRepository.countByStatusNot(IncidentStatus.CLOSED);

        // 4. Reglas activas - Asumiendo que quieres contar todas por ahora,
        // o countByIsActiveTrue() si tienes un booleano en la entidad.
        long activeRules = ruleRepository.count();

        return new DashboardSummaryDTO(totalEvents, activeAlerts, openIncidents, activeRules);
    }
}