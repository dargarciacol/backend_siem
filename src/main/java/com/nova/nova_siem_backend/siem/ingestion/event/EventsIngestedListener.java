package com.nova.nova_siem_backend.siem.ingestion.event;

import com.nova.nova_siem_backend.siem.detection.service.DetectionEngineService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EventsIngestedListener {

    private final DetectionEngineService detectionEngineService;

    public EventsIngestedListener(
            DetectionEngineService detectionEngineService) {
        this.detectionEngineService = detectionEngineService;
    }

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventsIngested(EventsIngestedEvent event) {

        detectionEngineService.evaluateEvents(event.eventIds());
    }
}