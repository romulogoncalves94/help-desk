package br.com.helpdesk.orderserviceapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import models.exceptions.StandardError;
import models.requests.CreatedOrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "OrderController", description = "Controller responsible for orders operations")
@RequestMapping("/api/orders")
public interface OrderController {

    @Operation(summary = "Criar uma ordem nova",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ordem criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)))
            })
    @PostMapping
    ResponseEntity<Void> save(@Valid @RequestBody final CreatedOrderRequest request);

}
