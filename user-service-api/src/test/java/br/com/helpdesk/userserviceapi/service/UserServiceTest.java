package br.com.helpdesk.userserviceapi.service;

import br.com.helpdesk.userserviceapi.entity.User;
import br.com.helpdesk.userserviceapi.mapper.UserMapper;
import br.com.helpdesk.userserviceapi.repository.UserRepository;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static br.com.helpdesk.userserviceapi.creator.CreatorUtil.generateMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Test
    @DisplayName("Quando chamar um FindById com ID válido, retornar um UserResponse")
    void whenCallFindByIdWithValidIdThenReturnUserResponse() {
        when(repository.findById(anyString())).thenReturn(Optional.of(new User()));
        when(mapper.fromEntity(any(User.class))).thenReturn(generateMock(UserResponse.class));

        final var response = service.findById("1");

        assertNotNull(response);
        assertEquals(UserResponse.class, response.getClass());

        verify(repository).findById(anyString());
        verify(mapper).fromEntity(any(User.class));
    }

    @Test
    @DisplayName("Quando chamar um FindById com ID inválido, retornar uma exceção do tipo ResourceNotFoundException")
    void whenCallFindByIdWithInvalidIdThenThrowResourceNotFoundException() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        try {
            service.findById("1");
        } catch (Exception e) {
            assertEquals("Usuário não encontrado. ID: " + "1" + " Tipo: " + "UserResponse", e.getMessage());
            assertEquals(ResourceNotFoundException.class, e.getClass());
        }

        verify(repository).findById(anyString());
        verify(mapper, times(0)).fromEntity(any(User.class));
    }

    @Test
    @DisplayName("Quando chamar um FindAll, retornar uma lista de UserResponse")
    void whenCallFindAllThenReturnListOfUserResponse() {
        when(repository.findAll()).thenReturn(List.of(new User(), new User()));
        when(mapper.fromEntity(any(User.class))).thenReturn(generateMock(UserResponse.class));

        final var response = service.findAll();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(UserResponse.class, response.get(0).getClass());

        verify(repository).findAll();
        verify(mapper, times(2)).fromEntity(any(User.class));
    }

    @Test
    @DisplayName("Quando chamar um Save com um CreateUserRequest válido, salvar o usuário")
    void whenCallSaveWithValidCreateUserRequestThenSaveUser() {
        final var request = generateMock(CreateUserRequest.class);

        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenReturn(new User());
        when(mapper.fromRequest(any(CreateUserRequest.class))).thenReturn(new User());
        when(encoder.encode(anyString())).thenReturn("encodedPassword");

        service.save(request);

        verify(mapper).fromRequest(request);
        verify(encoder).encode(request.password());
        verify(repository).findByEmail(request.email());
        verify(repository).save(any(User.class));
    }

}