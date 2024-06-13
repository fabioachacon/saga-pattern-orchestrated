package com.microservices.orchestrated.payment_service.core.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.microservices.orchestrated.payment_service.core.utils.JsonUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentConsumer {

    private final JsonUtil jsonUtil;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.payment-success}")
    public void consumeSuccessEvent(String payload) {
        handleConsume(payload, "Receiving success event {} from payment-success topic");
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.payment-fail}")
    public void consumeFailEvent(String payload) {
        handleConsume(payload, "Receiving rollback event {} from payment-fail topic");
    }

    private void handleConsume(String payload, String logMessage) {
        log.info(logMessage.formatted(payload));
        var event = jsonUtil.toEvent(payload);
        log.info(event.toString());
    }
}
