package com.nova.nova_siem_backend.siem.dashboard.dto;

public class DashboardSummaryDTO {

    private long totalEvents;
    private long activeIncidents;
    private long openAlerts;
    private long criticalIncidents;
    private long activeRules;

    public DashboardSummaryDTO(long totalEvents, long activeIncidents, long openAlerts, long criticalIncidents, long activeRules) {
        this.totalEvents = totalEvents;
        this.activeIncidents = activeIncidents;
        this.openAlerts = openAlerts;
        this.criticalIncidents = criticalIncidents;
        this.activeRules = activeRules;
    }

    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public long getActiveIncidents() {
        return activeIncidents;
    }

    public void setActiveIncidents(long activeIncidents) {
        this.activeIncidents = activeIncidents;
    }

    public long getOpenAlerts() {
        return openAlerts;
    }

    public void setOpenAlerts(long openAlerts) {
        this.openAlerts = openAlerts;
    }

    public long getCriticalIncidents() {
        return criticalIncidents;
    }

    public void setCriticalIncidents(long criticalIncidents) {
        this.criticalIncidents = criticalIncidents;
    }

    public long getActiveRules() {
        return activeRules;
    }

    public void setActiveRules(long activeRules) {
        this.activeRules = activeRules;
    }
}
