package br.com.helpdesk.orderserviceapi.services.impl;

import br.com.helpdesk.orderserviceapi.clients.UserServiceFeignClient;
import br.com.helpdesk.orderserviceapi.entities.Order;
import br.com.helpdesk.orderserviceapi.mapper.OrderMapper;
import br.com.helpdesk.orderserviceapi.repositories.OrderRepository;
import br.com.helpdesk.orderserviceapi.services.OrderService;
import lombok.RequiredArgsConstructor;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreatedOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import models.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static models.enums.OrderStatusEnum.CLOSED;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final UserServiceFeignClient userServiceFeignClient;

    @Override
    public void save(CreatedOrderRequest request) {
        final var requester = validateUserId(request.requesterId());
        final var customer = validateUserId(request.customerId());

        repository.save(mapper.fromRequest(request));
    }

    @Override
    public OrderResponse update(Long id, UpdateOrderRequest request) {
        validateUsers(request);
        
        Order entity = findById(id);
        entity = mapper.fromRequest(entity, request);

        if (CLOSED.getDescription().equals(entity.getStatus())) {
            entity.setClosedAt(now());
        }

        return mapper.fromEntity(repository.save(entity));
    }

    private void validateUsers(UpdateOrderRequest request) {
        if (nonNull(request.requesterId())) validateUserId(request.requesterId());
        if (nonNull(request.customerId())) validateUserId(request.customerId());
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

    @Override
    public Page<Order> findAllPaginated(Integer page, Integer linesPerPage, String direction, String orderBy) {
        PageRequest pageRequest = PageRequest.of(
                page,
                linesPerPage,
                Sort.Direction.valueOf(direction),
                orderBy
        );

        return repository.findAll(pageRequest);
    }

    private UserResponse validateUserId(final String userId) {
        return userServiceFeignClient.findById(userId).getBody();
    }
}
