package br.com.helpdesk.userserviceapi.service;

import br.com.helpdesk.userserviceapi.mapper.UserMapper;
import br.com.helpdesk.userserviceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserResponse findById(final String id) {
        return mapper.fromEntity(
                repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. ID: " + id + " Tipo: " + UserResponse.class.getSimpleName()))
        );
    }

    public void save(CreateUserRequest request) {
        repository.save(mapper.fromRequest(request));
    }
}
