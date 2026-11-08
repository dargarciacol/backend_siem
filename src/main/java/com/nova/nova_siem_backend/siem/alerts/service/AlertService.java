package com.nova.nova_siem_backend.siem.alerts.service;

import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus;
import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;

import com.nova.nova_siem_backend.siem.alerts.model.SecurityAlert;

import com.nova.nova_siem_backend.siem.alerts.repository.AlertRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public Page<SecurityAlert> getAllAlerts(Pageable pageable) {
        return alertRepository.findAll(pageable);
    }

    @Transactional
    public SecurityAlert updateAlertStatus(UUID id, AlertStatus status, String username) {
        SecurityAlert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with ID: " + id));

        alert.setStatus(status);
        if (status == AlertStatus.IN_PROGRESS || status == AlertStatus.UNDER_INVESTIGATION) {
            alert.setAssignedTo(username);
        }

        return alertRepository.save(alert);
    }
}