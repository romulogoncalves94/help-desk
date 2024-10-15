package br.com.helpdesk.userserviceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import models.exceptions.StandardError;
import models.responses.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController", description = "Controller responsible for user operations")
@RequestMapping("/api/users")
public interface UserController {

    @Operation(summary = "Find User By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(
                    responseCode = "404", description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)
                    ))
    })
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(
            @Parameter(description = "User ID", required = true)
            @PathVariable String id);

}
