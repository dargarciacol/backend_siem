package com.nova.nova_siem_backend.siem.dashboard.dto;

public class DashboardSummaryDTO {

    private long totalEvents;
    private long activeAlerts;    // Ajustado al nombre del frontend
    private long openIncidents;   // Ajustado al nombre del frontend
    private long activeRules;     // Reemplaza criticalIncidents para coincidir con el UI

    // Constructor vacío sugerido para facilitar la instanciación en el Service
    public DashboardSummaryDTO() {
    }

    public DashboardSummaryDTO(long totalEvents, long activeAlerts, long openIncidents, long activeRules) {
        this.totalEvents = totalEvents;
        this.activeAlerts = activeAlerts;
        this.openIncidents = openIncidents;
        this.activeRules = activeRules;
    }

    // Getters y Setters
    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public long getActiveAlerts() {
        return activeAlerts;
    }

    public void setActiveAlerts(long activeAlerts) {
        this.activeAlerts = activeAlerts;
    }

    public long getOpenIncidents() {
        return openIncidents;
    }

    public void setOpenIncidents(long openIncidents) {
        this.openIncidents = openIncidents;
    }

    public long getActiveRules() {
        return activeRules;
    }

    public void setActiveRules(long activeRules) {
        this.activeRules = activeRules;
    }
}