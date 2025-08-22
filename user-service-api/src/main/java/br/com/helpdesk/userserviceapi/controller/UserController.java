package br.com.helpdesk.userserviceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import models.exceptions.StandardError;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "UserController", description = "Controller responsável por gerenciar usuários.")
@RequestMapping("api/users")
public interface UserController {

    @Operation(summary = "Buscar usuário por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(
            @Parameter(description = "User ID", required = true, example = "689f3d3b875e70383bf9a82f")
            @PathVariable(name = "id") final String id);

    @Operation(summary = "Criar um novo usuário",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)))
            })
    @PostMapping
    ResponseEntity<Void> save(@Valid @RequestBody final CreateUserRequest request);

    @Operation(summary = "Lista de todos os usuários",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados", content = @Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping
    ResponseEntity<List<UserResponse>> findAll();

    @Operation(summary = "Atualizar usuário",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)))
            })
    @PutMapping("/{id}")
    ResponseEntity<UserResponse> update(@Parameter(description = "User id", required = true, example = "689f3d3b875e70383bf9a82f")
                                        @PathVariable(name = "id") final String id,
                                        @Valid @RequestBody final UpdateUserRequest request);

}
