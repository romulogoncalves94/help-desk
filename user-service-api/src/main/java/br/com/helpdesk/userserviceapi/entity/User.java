package br.com.helpdesk.userserviceapi.entity;

import lombok.*;
import models.enums.ProfileEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@With
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private Set<ProfileEnum> profiles;

}
