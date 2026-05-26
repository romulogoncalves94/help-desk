package br.com.helpdesk.orderserviceapi.services.impl;

import br.com.helpdesk.orderserviceapi.entities.Order;
import br.com.helpdesk.orderserviceapi.mapper.OrderMapper;
import br.com.helpdesk.orderserviceapi.repositories.OrderRepository;
import br.com.helpdesk.orderserviceapi.services.OrderService;
import lombok.RequiredArgsConstructor;
import models.enums.OrderStatusEnum;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreatedOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.LocalDateTime.now;
import static models.enums.OrderStatusEnum.CLOSED;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public void save(CreatedOrderRequest request) {
        repository.save(mapper.fromRequest(request));
    }

    @Override
    public OrderResponse update(Long id, UpdateOrderRequest request) {
        Order entity = findById(id);
        entity = mapper.fromRequest(entity, request);

        if (CLOSED.getDescription().equals(entity.getStatus())) {
            entity.setClosedAt(now());
        }

        return mapper.fromEntity(repository.save(entity));
    }

    @Override
    public Order findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order não encontrada Id: " + id + "Type: " + Order.class.getSimpleName()));
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }
}
