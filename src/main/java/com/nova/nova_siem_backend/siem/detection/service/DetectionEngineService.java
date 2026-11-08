package com.nova.nova_siem_backend.siem.detection.service;

import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus;
import com.nova.nova_siem_backend.siem.alerts.model.SecurityAlert;
import com.nova.nova_siem_backend.siem.alerts.repository.AlertRepository;
import com.nova.nova_siem_backend.siem.detection.model.DetectionRule;
import com.nova.nova_siem_backend.siem.detection.repository.DetectionRuleRepository;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import com.nova.nova_siem_backend.siem.events.repository.EventRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DetectionEngineService {

    private final DetectionRuleRepository ruleRepository;
    private final AlertRepository alertRepository;
    private final EventRepository eventRepository;

    public DetectionEngineService(
            DetectionRuleRepository ruleRepository,
            AlertRepository alertRepository,
            EventRepository eventRepository) {

        this.ruleRepository = ruleRepository;
        this.alertRepository = alertRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public void evaluateEvents(List<UUID> eventIds) {

        if (eventIds == null || eventIds.isEmpty()) {
            return;
        }

        List<SecurityEvent> events = eventRepository.findAllById(eventIds);
        List<DetectionRule> activeRules = ruleRepository.findByActiveTrue();

        for (DetectionRule rule : activeRules) {

            long newHits = 0;

            for (SecurityEvent event : events) {

                if (!evaluateLogic(rule.getSigmaLogic(), event)) {
                    continue;
                }

                boolean exists =
                        alertRepository.existsBySourceEvent_IdAndRuleTriggered(
                                event.getId(),
                                rule.getName()
                        );

                if (exists) {
                    continue;
                }

                SecurityAlert alert = new SecurityAlert();
                alert.setRuleTriggered(rule.getName());
                alert.setSeverity(rule.getSeverity());
                alert.setStatus(AlertStatus.NEW);
                alert.setSourceEvent(event);

                alertRepository.save(alert);
                newHits++;
            }

            if (newHits > 0) {
                Long currentHits = rule.getHitCount() != null
                        ? rule.getHitCount()
                        : 0L;

                rule.setHitCount(currentHits + newHits);
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