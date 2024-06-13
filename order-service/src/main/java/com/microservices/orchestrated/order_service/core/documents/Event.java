package com.microservices.orchestrated.order_service.core.documents;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "event")
public class Event {

    @Id
    private String id;
    private String orderId;
    private String transactionId;
    private String source;
    private String status;
    private Order payload;
    private List<History> eventHistory;
    private LocalDateTime createdAt;
}
