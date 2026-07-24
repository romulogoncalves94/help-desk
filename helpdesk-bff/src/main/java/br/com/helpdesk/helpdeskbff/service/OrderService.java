package br.com.helpdesk.helpdeskbff.service;

import br.com.helpdesk.helpdeskbff.client.OrderFeignClient;
import lombok.RequiredArgsConstructor;
import models.requests.CreatedOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderFeignClient client;

    public void save(CreatedOrderRequest request) {
        client.save(request);
    }

    public OrderResponse update(Long id, UpdateOrderRequest request) {
        return client.update(id, request).getBody();
    }

    public OrderResponse findById(Long id) {
        return client.findById(id).getBody();
    }

    public void deleteById(Long id) {
        client.deleteById(id);
    }

    public List<OrderResponse> findAll() {
        return client.findAll().getBody();
    }

    public Page<OrderResponse> findAllPaginated(Integer page, Integer linesPerPage, String direction, String orderBy) {
        return client.findAllPaginated(page, linesPerPage, direction, orderBy).getBody();
    }

}
