package br.com.helpdesk.authserviceapi.services;

import br.com.helpdesk.authserviceapi.models.RefreshToken;
import br.com.helpdesk.authserviceapi.repositories.RefreshTokenRepository;
import br.com.helpdesk.authserviceapi.security.dtos.UserDetailsDTO;
import br.com.helpdesk.authserviceapi.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import models.exceptions.RefreshTokenExpired;
import models.exceptions.ResourceNotFoundException;
import models.responses.RefreshTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.expiration-sec.refresh-token}")
    private Long refreshTokenExpirationSec;

    private final RefreshTokenRepository repository;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public RefreshToken save(final String username) {
        return repository.save(
                RefreshToken.builder()
                        .id(UUID.randomUUID().toString())
                        .createdAt(now())
                        .expiresAt(now().plusSeconds(refreshTokenExpirationSec))
                        .username(username)
                        .build()
        );
    }

    public RefreshTokenResponse refreshToken(final String refreshTokenId) {
        final var refreshToken = repository.findById(refreshTokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh Token não encontrado! Id: " + refreshTokenId));

        if (refreshToken.getExpiresAt().isBefore(now()))
            throw new RefreshTokenExpired("Refresh Token expirado! Id: " + refreshTokenId);

        return new RefreshTokenResponse(jwtUtils.generateToken((UserDetailsDTO) userDetailsService.loadUserByUsername(refreshToken.getUsername())));
    }

}
