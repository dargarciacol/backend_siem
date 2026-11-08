package com.nova.nova_siem_backend.siem.alerts.model;

public enum AlertStatus {
    NEW,
    IN_PROGRESS,
    UNDER_INVESTIGATION,
    ESCALATED,
    RESOLVED,
    FALSE_POSITIVE
}