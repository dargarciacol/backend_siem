
package com.nova.nova_siem_backend.siem.detection.service;

import com.nova.nova_siem_backend.siem.alerts.model.AlertStatus;
import com.nova.nova_siem_backend.siem.alerts.model.SecurityAlert;
import com.nova.nova_siem_backend.siem.alerts.repository.AlertRepository;
import com.nova.nova_siem_backend.siem.detection.model.DetectionRule;
import com.nova.nova_siem_backend.siem.detection.model.RuleCategory;
import com.nova.nova_siem_backend.siem.detection.repository.DetectionRuleRepository;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import com.nova.nova_siem_backend.siem.events.repository.EventRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class DetectionEngineService {

    private static final int MIN_DISTINCT_ACCOUNTS = 5;
    private static final long WINDOW_SECONDS = 60;

    private static final String RULE_NAME = "Password Spraying";

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

        List<DetectionRule> activeRules = ruleRepository.findByActiveTrue();

        DetectionRule passwordSprayingRule = activeRules.stream()
                .filter(rule -> rule.getCategory() == RuleCategory.CREDENTIAL_ACCESS)
                .filter(rule -> rule.getName() != null
                        && rule.getName().equalsIgnoreCase(RULE_NAME))
                .findFirst()
                .orElse(null);

        if (passwordSprayingRule == null) {
            return;
        }

        List<SecurityEvent> failedEvents =
                eventRepository.findByEventTypeAndActionOrderByTimestampAsc(
                        "Security",
                        "Failure"
                );

        Map<String, List<SecurityEvent>> eventsByIp = new LinkedHashMap<>();

        for (SecurityEvent event : failedEvents) {

            if (event.getSourceIp() == null || event.getSourceIp().isBlank()) {
                continue;
            }

            if (event.getUserIdentity() == null || event.getUserIdentity().isBlank()) {
                continue;
            }

            eventsByIp.computeIfAbsent(
                    event.getSourceIp().trim(),
                    key -> new ArrayList<>()
            ).add(event);
        }

        long newHits = 0;

        for (Map.Entry<String, List<SecurityEvent>> entry : eventsByIp.entrySet()) {

            String sourceIp = entry.getKey();

            List<SecurityEvent> ipEvents = entry.getValue();

            ipEvents.sort(Comparator.comparing(SecurityEvent::getTimestamp));

            int start = 0;

            while (start < ipEvents.size()) {

                SecurityEvent firstEvent = ipEvents.get(start);

                Set<String> distinctAccounts = new HashSet<>();

                SecurityEvent representativeEvent = firstEvent;

                int end = start;

                while (end < ipEvents.size()) {

                    SecurityEvent currentEvent = ipEvents.get(end);

                    long seconds = Duration.between(
                            firstEvent.getTimestamp(),
                            currentEvent.getTimestamp()
                    ).getSeconds();

                    if (seconds > WINDOW_SECONDS) {
                        break;
                    }

                    String account = currentEvent.getUserIdentity()
                            .trim()
                            .toLowerCase(Locale.ROOT);

                    distinctAccounts.add(account);

                    end++;
                }

                if (distinctAccounts.size() >= MIN_DISTINCT_ACCOUNTS) {

                    String correlationKey = buildCorrelationKey(
                            sourceIp,
                            firstEvent
                    );

                    boolean alreadyExists =
                            alertRepository.existsByCorrelationKey(correlationKey);

                    if (!alreadyExists) {

                        SecurityAlert alert = new SecurityAlert();

                        alert.setRuleTriggered(passwordSprayingRule.getName());
                        alert.setSeverity(passwordSprayingRule.getSeverity());
                        alert.setStatus(AlertStatus.NEW);
                        alert.setSourceEvent(representativeEvent);
                        alert.setCorrelationKey(correlationKey);

                        alertRepository.save(alert);

                        newHits++;
                    }
                }

                start = end;
            }
        }

        if (newHits > 0) {

            Long currentHits = passwordSprayingRule.getHitCount() != null
                    ? passwordSprayingRule.getHitCount()
                    : 0L;

            passwordSprayingRule.setHitCount(currentHits + newHits);

            ruleRepository.save(passwordSprayingRule);
        }
    }

    private String buildCorrelationKey(
            String sourceIp,
            SecurityEvent firstEvent) {

        return "PASSWORD_SPRAYING|"
                + sourceIp
                + "|"
                + firstEvent.getTimestamp();
    }
}
