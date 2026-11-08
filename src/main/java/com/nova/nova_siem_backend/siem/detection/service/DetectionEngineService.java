
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class DetectionEngineService {

    private static final int MIN_DISTINCT_ACCOUNTS = 5;
    private static final Duration DETECTION_WINDOW = Duration.ofSeconds(60);
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

        evaluatePasswordSpraying();
    }

    @Transactional
    public void reprocessHistoricalEvents() {
        evaluatePasswordSpraying();
    }

    private void evaluatePasswordSpraying() {

        DetectionRule passwordSprayingRule = ruleRepository.findByActiveTrue()
                .stream()
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

            if (event.getTimestamp() == null
                    || event.getSourceIp() == null
                    || event.getSourceIp().isBlank()
                    || event.getUserIdentity() == null
                    || event.getUserIdentity().isBlank()) {
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

            Map<String, Integer> accountCounts = new HashMap<>();

            int start = 0;
            int end = 0;

            SecurityEvent episodeRepresentative = null;
            java.time.LocalDateTime episodeEnd = null;

            while (start < ipEvents.size()) {

                if (end < start) {
                    end = start;
                }

                while (end < ipEvents.size()) {

                    SecurityEvent currentEvent = ipEvents.get(end);

                    Duration elapsed = Duration.between(
                            ipEvents.get(start).getTimestamp(),
                            currentEvent.getTimestamp()
                    );

                    if (elapsed.compareTo(DETECTION_WINDOW) > 0) {
                        break;
                    }

                    String account = normalizeAccount(currentEvent);

                    accountCounts.merge(account, 1, Integer::sum);

                    end++;
                }

                if (accountCounts.size() >= MIN_DISTINCT_ACCOUNTS) {

                    SecurityEvent windowStart = ipEvents.get(start);
                    SecurityEvent windowEnd = ipEvents.get(end - 1);

                    if (episodeEnd == null
                            || windowStart.getTimestamp().isAfter(episodeEnd)) {

                        episodeRepresentative = windowStart;

                        String correlationKey = buildCorrelationKey(
                                sourceIp,
                                episodeRepresentative
                        );

                        boolean alreadyExists =
                                alertRepository.existsByCorrelationKey(correlationKey);

                        if (!alreadyExists) {

                            SecurityAlert alert = new SecurityAlert();

                            alert.setRuleTriggered(passwordSprayingRule.getName());
                            alert.setSeverity(passwordSprayingRule.getSeverity());
                            alert.setStatus(AlertStatus.NEW);
                            alert.setSourceEvent(episodeRepresentative);
                            alert.setCorrelationKey(correlationKey);

                            alertRepository.save(alert);

                            newHits++;
                        }
                    }

                    episodeEnd = windowEnd.getTimestamp();
                }

                if (start < end) {

                    String account = normalizeAccount(ipEvents.get(start));

                    accountCounts.computeIfPresent(account, (key, count) ->
                            count == 1 ? null : count - 1
                    );
                }

                start++;
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

    private String normalizeAccount(SecurityEvent event) {
        return event.getUserIdentity()
                .trim()
                .toLowerCase(Locale.ROOT);
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
