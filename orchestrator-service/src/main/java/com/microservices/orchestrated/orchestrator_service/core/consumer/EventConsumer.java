package com.microservices.orchestrated.orchestrator_service.core.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.microservices.orchestrated.orchestrator_service.core.utils.JsonUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class EventConsumer {
    private final JsonUtil jsonUtil;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.start-saga}")
    public void consumeStartSagaEvent(String payload) {
        handleConsume(payload, "Receiving event {} from start saga topic");
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.orchestrator}")
    public void consumeOrchestratorEvent(String payload) {
        handleConsume(payload, "Receiving event {} from orchestrator topic");
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.finish-success}")
    public void consumeFinishSuccessEvent(String payload) {
        handleConsume(payload, "Receiving event {} from finish success topic");
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.finish-fail}")
    public void consumeFinishFailEvent(String payload) {
        handleConsume(payload, "Receiving event {} from finish fail topic");

    }

    private void handleConsume(String payload, String logMessage) {
        log.info(logMessage.formatted(payload));
        var event = jsonUtil.toEvent(payload);
        log.info(event.toString());
    }
}
