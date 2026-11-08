
package com.nova.nova_siem_backend.siem.detection.service;

import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import com.nova.nova_siem_backend.siem.common.exception.ResourceNotFoundException;
import com.nova.nova_siem_backend.siem.detection.dto.DetectionRuleRequestDTO;
import com.nova.nova_siem_backend.siem.detection.mapper.DetectionRuleMapper;
import com.nova.nova_siem_backend.siem.detection.model.DetectionRule;
import com.nova.nova_siem_backend.siem.detection.repository.DetectionRuleRepository;
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
    private final DetectionRuleMapper ruleMapper;

    public DetectionRuleService(
            DetectionRuleRepository ruleRepository,
            DetectionEngineService detectionEngineService,
            EventRepository eventRepository,
            DetectionRuleMapper ruleMapper) {
        this.ruleMapper = ruleMapper;
        this.ruleRepository = ruleRepository;
        this.detectionEngineService = detectionEngineService;
        this.eventRepository = eventRepository;
    }

    public List<DetectionRule> getAllRules() {
        return ruleRepository.findAll();
    }

    @Transactional
    public DetectionRule createRule(DetectionRuleRequestDTO request) {
        String name = request.getName().trim();

        if (ruleRepository.existsByNameIgnoreCase(name)) {
            throw new BusinessException("A detection rule named '" + name + "' already exists.");
        }

        DetectionRule rule = ruleMapper.toEntity(request);
        rule.setName(name);

        return ruleRepository.save(rule);
    }

    @Transactional
    public DetectionRule toggleRuleStatus(UUID id) {
        DetectionRule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));

        rule.setActive(!rule.isActive());

        return ruleRepository.save(rule);
    }

    @Transactional
    public void reprocessHistoricalEvents() {
        // The engine evaluates the stored events itself; loading every event
        // into memory just to collect their IDs was unnecessary.
        if (eventRepository.count() == 0) {
            return;
        }

        detectionEngineService.reprocessHistoricalEvents();
    }
}
