package br.com.helpdesk.orderserviceapi.services;

import models.requests.CreatedOrderRequest;

public interface OrderService {
    void save(CreatedOrderRequest request);
}
