package com.nova.nova_siem_backend.siem.detection.service;

import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus;
import com.nova.nova_siem_backend.siem.alerts.model.SecurityAlert;
import com.nova.nova_siem_backend.siem.alerts.repository.AlertRepository;
import com.nova.nova_siem_backend.siem.detection.model.DetectionRule;
import com.nova.nova_siem_backend.siem.detection.repository.DetectionRuleRepository;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetectionEngineService {

    private final DetectionRuleRepository ruleRepository;
    private final AlertRepository alertRepository;

    public DetectionEngineService(DetectionRuleRepository ruleRepository, AlertRepository alertRepository) {
        this.ruleRepository = ruleRepository;
        this.alertRepository = alertRepository;
    }

    @Async("taskExecutor")
    @Transactional
    public void evaluateEvent(SecurityEvent event) {
        List<DetectionRule> activeRules = ruleRepository.findByActiveTrue();

        for (DetectionRule rule : activeRules) {
            if (evaluateLogic(rule.getSigmaLogic(), event)) {

                SecurityAlert alert = new SecurityAlert();
                alert.setRuleTriggered(rule.getName());
                alert.setSeverity(rule.getSeverity());
                alert.setStatus(AlertStatus.NEW);
                alert.setSourceEvent(event);

                alertRepository.save(alert);

                rule.setHitCount(rule.getHitCount() + 1);
                ruleRepository.save(rule);
            }
        }
    }


    private boolean evaluateLogic(String sigmaLogic, SecurityEvent event) {

        if (sigmaLogic == null || event == null) {
            return false;
        }

        String action = event.getAction() != null
                ? event.getAction().toLowerCase()
                : "";

        String eventType = event.getEventType() != null
                ? event.getEventType().toLowerCase()
                : "";

        String logic = sigmaLogic.toLowerCase();

        return (action.length() > 2 && logic.contains(action))
                || (eventType.length() > 2 && logic.contains(eventType));
    }
}