package com.microservices.orchestrated.product_validation_service.core.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.microservices.orchestrated.product_validation_service.core.utils.JsonUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class ProductValidationConsumer {

    private final JsonUtil jsonUtil;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.product-validation-success}")
    public void consumeSuccessEvent(String payload) {
        handleConsume(payload, "Receiving success event {} from product-validation-success topic");
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.product-validation-fail}")
    public void consumeFailEvent(String payload) {
        handleConsume(payload, "Receiving rollback event {} from product-validation-fail topic");
    }

    private void handleConsume(String payload, String logMessage) {
        log.info(logMessage.formatted(payload));
        var event = jsonUtil.toEvent(payload);
        log.info(event.toString());
    }
}
