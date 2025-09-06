package br.com.helpdesk.authserviceapi.models;

import lombok.Builder;
import lombok.Getter;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Builder
public class RefreshToken {

    @Id
    private String id;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

}
