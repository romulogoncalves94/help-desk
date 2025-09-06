package br.com.helpdesk.authserviceapi.controller.impl;

import br.com.helpdesk.authserviceapi.controller.AuthController;
import br.com.helpdesk.authserviceapi.security.JWTAuthenticationImpl;
import br.com.helpdesk.authserviceapi.services.RefreshTokenService;
import br.com.helpdesk.authserviceapi.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import models.requests.AuthenticateRequest;
import models.requests.RefreshTokenRequest;
import models.responses.AuthenticationResponse;
import models.responses.RefreshTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final JwtUtils jwtUtils;
    private final AuthenticationConfiguration configuration;
    private final RefreshTokenService refreshTokenService;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(final AuthenticateRequest request) throws Exception {
        return ResponseEntity.ok().body(
                new JWTAuthenticationImpl(jwtUtils, configuration.getAuthenticationManager())
                        .authenticate(request)
                        .withRefreshToken(refreshTokenService.save(request.email()).getId())
        );
    }

    @Override
    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        return ResponseEntity.ok().body(refreshTokenService.refreshToken(request.refreshToken()));
    }

}
