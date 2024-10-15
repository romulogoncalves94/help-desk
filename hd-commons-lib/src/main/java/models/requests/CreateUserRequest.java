package models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.With;
import models.enums.ProfileEnum;

import java.util.Set;

@With
public record CreateUserRequest(
        @NotBlank(message = "Name cannot be empty")
        @Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
        @Schema(description = "User name", example = "Rômulo Gonçalves")
        String name,

        @NotBlank(message = "Email cannot be empty")
        @Size(min = 6, max = 80, message = "Email must be between 6 and 80 characters")
        @Schema(description = "User email", example = "romulo@romulo.com")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 6, max = 80, message = "Password must be between 6 and 80 characters")
        @Schema(description = "User password", example = "123456")
        String password,

        @Schema(description = "User profiles", example = "[\"ROLE_ADMIN\", \"ROLE_TECHNICIAN\"]")
        Set<ProfileEnum> profiles
) {
}
