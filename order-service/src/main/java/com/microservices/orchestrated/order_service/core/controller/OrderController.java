package com.microservices.orchestrated.order_service.core.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.orchestrated.order_service.core.documents.Order;
import com.microservices.orchestrated.order_service.core.dto.OrderRequest;
import com.microservices.orchestrated.order_service.core.service.OrderService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService service;

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest orderRequest) {
        return service.createOrder(orderRequest);
    }

}
