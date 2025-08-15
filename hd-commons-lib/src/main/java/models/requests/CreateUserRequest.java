package models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.With;
import models.enums.ProfileEnum;

import java.util.Set;

@With
public record CreateUserRequest(
        @Schema(description = "Nome do usuário", example = "João da Silva")
        @NotBlank(message = "O campo nome não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O campo nome deve ter entre 3 e 100 caracteres.")
        String name,

        @Schema(description = "Email do usuário", example = "teste@gmail.com")
        @Email
        @NotBlank(message = "O campo E-mail não pode ser vazio.")
        @Size(min = 6, max = 50, message = "O campo E-mail deve ter entre 6 e 50 caracteres.")
        String email,

        @Schema(description = "Senha do usuário", example = "senha123")
        @Size(min = 6, max = 20, message = "O campo senha deve ter entre 6 e 20 caracteres.")
        @NotBlank(message = "O campo senha não pode ser vazio.")
        String password,

        @Schema(description = "Perfis do usuário", example = "[\"ROLE_ADMIN\", \"ROLE_CUSTOMER\"]")
        Set<ProfileEnum> profiles
) {}
