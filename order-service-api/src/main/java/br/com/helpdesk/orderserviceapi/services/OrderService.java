package br.com.helpdesk.orderserviceapi.services;

import br.com.helpdesk.orderserviceapi.entities.Order;
import models.requests.CreatedOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    void save(CreatedOrderRequest request);
    OrderResponse update(final Long id, UpdateOrderRequest request);
    Order findById(final Long id);
    void deleteById(final Long id);
    List<Order> findAll();
    Page<Order> findAllPaginated(Integer page, Integer linesPerPage, String direction, String orderBy);
}
