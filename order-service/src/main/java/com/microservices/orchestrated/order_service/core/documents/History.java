package com.microservices.orchestrated.order_service.core.documents;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {
    private String source;
    private String status;
    private String message;
    private LocalDateTime createdAt;
}
