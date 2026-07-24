package br.com.helpdesk.helpdeskbff.controller.impl;

import br.com.helpdesk.helpdeskbff.controller.OrderController;
import br.com.helpdesk.helpdeskbff.service.OrderService;
import lombok.RequiredArgsConstructor;
import models.requests.CreatedOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class OrderContollerImpl implements OrderController {

    private final OrderService service;

    @Override
    public ResponseEntity<Void> save(CreatedOrderRequest request) {
        service.save(request);
        return ResponseEntity.status(CREATED).build();
    }

    @Override
    public ResponseEntity<OrderResponse> update(final Long id, UpdateOrderRequest request) {
        return ResponseEntity.ok().body(service.update(id, request));
    }

    @Override
    public ResponseEntity<OrderResponse> findById(Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(final Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @Override
    public ResponseEntity<Page<OrderResponse>> findAllPaginated(Integer page, Integer linesPerPage, String direction, String orderBy) {
        return ResponseEntity.ok().body(service.findAllPaginated(page, linesPerPage, direction, orderBy));
    }

}
