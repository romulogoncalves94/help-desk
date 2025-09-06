package models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequest(
        @Size(min = 16, max = 50, message = "Refresh token deve ter entre 16 e 36 caracteres.")
        @NotBlank(message = "Refresh token não pode ser vazio.")
        String refreshToken
) {
}
