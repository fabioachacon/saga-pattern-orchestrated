package com.microservices.orchestrated.inventory_service.core.model;

import java.time.LocalDateTime;

import com.microservices.orchestrated.inventory_service.core.enums.EventSource;
import com.microservices.orchestrated.inventory_service.core.enums.SagaStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {
    private String message;
    private EventSource source;
    private SagaStatus status;
    private LocalDateTime createdAt;
}
