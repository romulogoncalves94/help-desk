package br.com.helpdesk.orderserviceapi.services.impl;

import br.com.helpdesk.orderserviceapi.mapper.OrderMapper;
import br.com.helpdesk.orderserviceapi.repositories.OrderRepository;
import br.com.helpdesk.orderserviceapi.services.OrderService;
import lombok.RequiredArgsConstructor;
import models.requests.CreatedOrderRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public void save(CreatedOrderRequest request) {
        repository.save(mapper.fromRequest(request));
    }
}
