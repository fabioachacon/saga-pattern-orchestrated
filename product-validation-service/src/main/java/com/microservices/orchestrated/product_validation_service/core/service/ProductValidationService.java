package com.microservices.orchestrated.product_validation_service.core.service;

import org.springframework.stereotype.Service;
import static org.springframework.util.ObjectUtils.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.microservices.orchestrated.product_validation_service.core.enums.SagaStatus;
import com.microservices.orchestrated.product_validation_service.core.model.Event;
import com.microservices.orchestrated.product_validation_service.core.model.History;
import com.microservices.orchestrated.product_validation_service.core.model.OrderProduct;

import com.microservices.orchestrated.product_validation_service.core.model.Validation;
import com.microservices.orchestrated.product_validation_service.core.producer.KafkaProducer;
import com.microservices.orchestrated.product_validation_service.core.repository.ProductRepository;
import com.microservices.orchestrated.product_validation_service.core.repository.ValidationRepository;
import com.microservices.orchestrated.product_validation_service.core.utils.JsonUtil;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ProductValidationService {

    private static final String CURRENT_SOURCE = "PRODUCT_VALIDATION_SERVICE";

    private final JsonUtil jsonUtil;
    private final KafkaProducer producer;
    private final ProductRepository productRepository;
    private final ValidationRepository validationRepository;

    public void validateExistingProducts(Event event) {
        try {
            checkCurrentValidation(event);
            createValidation(event, true);
            handleSuccess(event);
        } catch (Exception e) {
            log.error("Error trying to validate products: ", e.getStackTrace().toString());
            handleFailCurrentNotExecuted(event, e.getMessage());
        }
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void checkCurrentValidation(Event event) {
        validateProductsInformed(event);
        validateExistingTransaction(event);
        validateProducts(event);

    }

    private void validateProductsInformed(Event event) {
        if (isEmpty(event.getPaylod()) || isEmpty(event.getPaylod().getProducts())) {
            throw new ValidationException("Product list is empty");
        }

        if (isEmpty(event.getPaylod().getId()) || isEmpty(event.getPaylod().getTransactionId())) {
            throw new ValidationException("OrderID and TransactionID must be informed");
        }
    }

    private void validateExistingTransaction(Event event) {
        if (isTransactionDuplicated(event)) {
            throw new ValidationException("There is an existing transationId for this validation.");
        }
    }

    private boolean isTransactionDuplicated(Event event) {
        return validationRepository.existsByOrderIdAndTransactionId(event.getOrderId(), event.getTransactionId());
    }

    private void validateProducts(Event event) {
        event.getPaylod().getProducts().forEach(product -> {
            validateProductInformed(product);
            validateExistingProduct(product.getProduct().getCode());
        });
    }

    private void validateProductInformed(OrderProduct orderProduct) {
        if (isEmpty(orderProduct.getProduct()) || isEmpty(orderProduct.getProduct().getCode())) {
            throw new ValidationException("Product must be informed");
        }
    }

    private void validateExistingProduct(String code) {
        if (!productRepository.existsByCode(code)) {
            throw new ValidationException("Product does not exist in the data base.");
        }
    }

    private void createValidation(Event event, boolean success) {
        var validation = Validation
                .builder()
                .orderId(event.getPaylod().getId())
                .transactionId(event.getTransactionId())
                .success(success)
                .build();

        validationRepository.save(validation);
    }

    private void handleSuccess(Event event) {
        event.setStatus(SagaStatus.SUCCESS);
        event.setSource(CURRENT_SOURCE);
        addToHistory(event, "Products are validated successfuly");
    }

    private void addToHistory(Event event, String message) {
        var history = History
                .builder()
                .source(event.getSource())
                .status(event.getStatus())
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        addEventHistory(event, history);

    }

    private void addEventHistory(Event event, History history) {
        var eventHistory = event.getEventHistory();
        if (isEmpty(eventHistory)) {
            event.setEventHistory(new ArrayList<>());
        }
        event.getEventHistory().add(history);
    }

    private void handleFailCurrentNotExecuted(Event event, String message) {
        event.setStatus(SagaStatus.ROLLBACK_PENDING);
        event.setSource(CURRENT_SOURCE);
        addToHistory(event, "Fail to validate products: ".concat(message));
    }

    public void rollbackEvent(Event event) {
        changeValidationToFail(event);
        event.setStatus(SagaStatus.FAIL);
        event.setSource(CURRENT_SOURCE);
        addToHistory(event, "Rollback execute on product validation!");
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void changeValidationToFail(Event event) {
        var result = validationRepository.findByOrderIdAndTransactionId(
                event.getPaylod().getId(),
                event.getTransactionId());

        result.ifPresentOrElse(
                (validation) -> {
                    validation.setSuccess(false);
                    validationRepository.save(validation);
                },
                () -> {
                    createValidation(event, false);
                });
    }

}
