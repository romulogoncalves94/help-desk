package br.com.helpdesk.authserviceapi.utils;

import br.com.helpdesk.authserviceapi.security.dtos.UserDetailsDTO;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(final UserDetailsDTO detailsDTO) {
        return Jwts.builder()
                .claim("id", detailsDTO.getId())
                .claim("name", detailsDTO.getName())
                .claim("authorities", detailsDTO.getAuthorities())
                .setSubject(detailsDTO.getUsername())
                .signWith(HS512, secret.getBytes())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

}
