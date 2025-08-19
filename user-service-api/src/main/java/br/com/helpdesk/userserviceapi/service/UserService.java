package br.com.helpdesk.userserviceapi.service;

import br.com.helpdesk.userserviceapi.entity.User;
import br.com.helpdesk.userserviceapi.mapper.UserMapper;
import br.com.helpdesk.userserviceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder;

    public UserResponse findById(final String id) {
        return mapper.fromEntity(find(id));
    }

    public void save(CreateUserRequest request) {
        verifyIfEmailAlreadyExists(request.email(), null);
        repository.save(
                mapper.fromRequest(request)
                        .withPassword(encoder.encode(request.password()))
        );
    }

    public void verifyIfEmailAlreadyExists(final String email, final String id) {
        repository.findByEmail(email)
                .filter(user -> !user.getId().equals(id))
                .ifPresent(user -> {
                    throw new DataIntegrityViolationException("Email já cadastrado: " + email);
                });
    }

    public List<UserResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::fromEntity)
                .toList();
    }

    public UserResponse update(final String id, final UpdateUserRequest request) {
        var entity = find(id);
        verifyIfEmailAlreadyExists(request.email(), id);
        return mapper.fromEntity(
                repository.save(
                        mapper.update(request, entity)
                                .withPassword(nonNull(request.password()) ? encoder.encode(request.password()) : entity.getPassword())
                ));
    }

    private User find(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. ID: " + id + " Tipo: " + UserResponse.class.getSimpleName()));
    }
}
