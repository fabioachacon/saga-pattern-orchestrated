package com.microservices.orchestrated.order_service.core.dto;

import java.util.List;

import com.microservices.orchestrated.order_service.core.documents.OrderProduct;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private List<OrderProduct> products;
}
