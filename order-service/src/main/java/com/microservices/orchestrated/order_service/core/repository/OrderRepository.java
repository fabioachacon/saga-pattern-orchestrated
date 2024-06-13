package com.microservices.orchestrated.order_service.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservices.orchestrated.order_service.core.documents.Order;

public interface OrderRepository extends MongoRepository<Order, String> {

}
