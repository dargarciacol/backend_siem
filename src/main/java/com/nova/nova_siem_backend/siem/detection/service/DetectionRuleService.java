
package com.nova.nova_siem_backend.siem.detection.service;

import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.detection.model.DetectionRule;
import com.nova.nova_siem_backend.siem.detection.repository.DetectionRuleRepository;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import com.nova.nova_siem_backend.siem.events.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DetectionRuleService {

    private final DetectionRuleRepository ruleRepository;
    private final DetectionEngineService detectionEngineService;
    private final EventRepository eventRepository;

    public DetectionRuleService(
            DetectionRuleRepository ruleRepository,
            DetectionEngineService detectionEngineService,
            EventRepository eventRepository) {
        this.ruleRepository = ruleRepository;
        this.detectionEngineService = detectionEngineService;
        this.eventRepository = eventRepository;
    }

    public List<DetectionRule> getAllRules() {
        return ruleRepository.findAll();
    }

    public DetectionRule createRule(DetectionRule rule) {
        return ruleRepository.save(rule);
    }

    public DetectionRule toggleRuleStatus(UUID id) {
        DetectionRule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));

        rule.setActive(!rule.isActive());

        return ruleRepository.save(rule);
    }

    @Transactional
    public void reprocessHistoricalEvents() {
        List<UUID> eventIds = eventRepository.findAll()
                .stream()
                .map(SecurityEvent::getId)
                .toList();

        if (eventIds.isEmpty()) {
            return;
        }

        detectionEngineService.evaluateEvents(eventIds);
    }
}
