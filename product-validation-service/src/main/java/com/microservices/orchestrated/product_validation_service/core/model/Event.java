package com.microservices.orchestrated.product_validation_service.core.model;

import java.time.LocalDateTime;
import java.util.List;

import com.microservices.orchestrated.product_validation_service.core.enums.SagaStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String id;
    private String orderId;
    private String transactionId;
    private Order paylod;
    private String source;
    private SagaStatus status;
    private List<History> eventHistory;
    private LocalDateTime createdAt;
}
