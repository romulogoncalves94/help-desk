package br.com.helpdesk.helpdeskbff.service;

import br.com.helpdesk.helpdeskbff.client.OrderFeignClient;
import lombok.RequiredArgsConstructor;
import models.requests.CreatedOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderFeignClient client;

    @CacheEvict(value = "orders", allEntries = true)
    public void save(CreatedOrderRequest request) {
        client.save(request);
    }

    @CacheEvict(value = "orders", allEntries = true)
    public OrderResponse update(Long id, UpdateOrderRequest request) {
        return client.update(id, request).getBody();
    }

    @Cacheable(value = "orders", key = "#id")
    public OrderResponse findById(Long id) {
        return client.findById(id).getBody();
    }

    @CacheEvict(value = "orders", allEntries = true)
    public void deleteById(Long id) {
        client.deleteById(id);
    }

    @Cacheable(value = "orders")
    public List<OrderResponse> findAll() {
        return client.findAll().getBody();
    }

    @Cacheable(value = "orders")
    public Page<OrderResponse> findAllPaginated(Integer page, Integer linesPerPage, String direction, String orderBy) {
        return client.findAllPaginated(page, linesPerPage, direction, orderBy).getBody();
    }

}
