package models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;

public record AuthenticateRequest(
        @Schema(description = "Email do usuário", example = "teste@gmail.com")
        @Email
        @NotBlank(message = "O campo E-mail não pode ser vazio.")
        @Size(min = 6, max = 50, message = "O campo E-mail deve ter entre 6 e 50 caracteres.")
        String email,

        @Schema(description = "Senha do usuário", example = "senha123")
        @Size(min = 6, max = 20, message = "O campo senha deve ter entre 6 e 20 caracteres.")
        @NotBlank(message = "O campo senha não pode ser vazio.")
        String password
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
