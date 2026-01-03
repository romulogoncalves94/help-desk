package br.com.helpdesk.orderserviceapi.controllers.impl;

import br.com.helpdesk.orderserviceapi.controllers.OrderController;
import br.com.helpdesk.orderserviceapi.services.OrderService;
import lombok.RequiredArgsConstructor;
import models.requests.CreatedOrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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

}
