package com.microservices.orchestrated.order_service.core.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microservices.orchestrated.order_service.config.exception.ValidationException;
import com.microservices.orchestrated.order_service.core.documents.Event;
import com.microservices.orchestrated.order_service.core.dto.EventFilters;
import com.microservices.orchestrated.order_service.core.repository.EventRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository repository;

    public void notifyEnding(Event event) {
        event.setOrderId(event.getOrderId());
        event.setCreatedAt(LocalDateTime.now());
        save(event);

        log.info("Order {} with saga notified! TransactionId: {}", event.getOrderId(), event.getTransactionId());
    }

    public Event save(Event event) {
        return repository.save(event);
    }

    public List<Event> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Event findByFilters(EventFilters filters) {
        validateEmptyFilters(filters);
        if (!isEmpty(filters.getOrderId())) {
            return findByOrderId(filters.getOrderId());
        } else {
            return findByTransactionId(filters.getTransactionId());
        }
    }

    private void validateEmptyFilters(EventFilters filters) {
        if (isEmptyFilter(filters)) {
            throw new ValidationException("OrderID or TransactionID must be provided");
        }
    }

    private Event findByOrderId(String orderId) {
        return repository
                .findTop1ByOrderIdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new ValidationException("Event not found by orderID"));
    }

    private Event findByTransactionId(String transactionId) {
        return repository
                .findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId)
                .orElseThrow(() -> new ValidationException("Event not found by TransactionId"));
    }

    private boolean isEmptyFilter(EventFilters filters) {
        return isEmpty(filters.getOrderId()) && isEmpty(filters.getTransactionId());
    }
}
