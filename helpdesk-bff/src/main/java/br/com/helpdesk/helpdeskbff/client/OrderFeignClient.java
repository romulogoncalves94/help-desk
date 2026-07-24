package br.com.helpdesk.helpdeskbff.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import models.requests.CreatedOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "order-service-api",
        path = "/api/orders"
)
public interface OrderFeignClient {
    @PostMapping
    ResponseEntity<Void> save(@Valid @RequestBody final CreatedOrderRequest request);

    @PutMapping("/{id}")
    ResponseEntity<OrderResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderRequest request
    );

    @GetMapping("/{id}")
    ResponseEntity<OrderResponse> findById(
            @NotNull(message = "O id da Ordem precisa ser informado")
            @PathVariable final Long id
    );

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(
            @NotNull(message = "O id da Ordem precisa ser informado")
            @PathVariable final Long id
    );

    @GetMapping
    ResponseEntity<List<OrderResponse>> findAll();

    @GetMapping("/page")
    ResponseEntity<Page<OrderResponse>> findAllPaginated(
            @RequestParam(name = "page", defaultValue = "0") final Integer page,
            @RequestParam(name = "linesPerPage", defaultValue = "10") final Integer linesPerPage,
            @RequestParam(name = "direction", defaultValue = "ASC") final String direction,
            @RequestParam(name = "orderBy", defaultValue = "id") final String orderBy
    );
}
