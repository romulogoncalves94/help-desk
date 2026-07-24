package br.com.helpdesk.helpdeskbff.service;

import br.com.helpdesk.helpdeskbff.client.AuthFeignClient;
import lombok.RequiredArgsConstructor;
import models.requests.AuthenticateRequest;
import models.requests.RefreshTokenRequest;
import models.responses.AuthenticationResponse;
import models.responses.RefreshTokenResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthFeignClient client;

    public AuthenticationResponse authenticate(AuthenticateRequest request) throws Exception {
        return client.authenticate(request).getBody();
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        return client.refreshToken(request).getBody();
    }

}
