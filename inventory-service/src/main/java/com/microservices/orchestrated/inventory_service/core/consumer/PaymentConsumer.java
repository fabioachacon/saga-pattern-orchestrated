package com.microservices.orchestrated.inventory_service.core.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.microservices.orchestrated.inventory_service.core.utils.JsonUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentConsumer {

    private final JsonUtil jsonUtil;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.inventory-success}")
    public void consumeSuccessEvent(String payload) {
        handleConsume(payload, String.format("Receiving success event %s from inventory-success topic", payload));
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.inventory-fail}")
    public void consumeFailEvent(String payload) {
        handleConsume(payload, String.format("Receiving rollback event %s from inventory-fail topic", payload));
    }

    private void handleConsume(String payload, String logMessage) {
        log.info(logMessage);
        var event = jsonUtil.toEvent(payload);
        log.info(event.toString());
    }
}
