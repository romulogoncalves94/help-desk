package br.com.helpdesk.userserviceapi.controller.impl;

import br.com.helpdesk.userserviceapi.entity.User;
import br.com.helpdesk.userserviceapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static br.com.helpdesk.userserviceapi.creator.CreatorUtil.generateMock;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerImplTest {

    public static final String BASE_URI = "/api/users";
    public static final String UPDATE_URI = BASE_URI + "/{id}";
    public static final String VALID_EMAIL = "kj45klj23b5@mail.com";
    public static final String VALIDATION_EXCEPTION_MSG = "Validation Exception";
    public static final String VALIDATION_ATTRIBUTES_ERROR_MSG = "Exception in validation attributes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void testFindByIdWithSuccess() throws Exception {
        final var entity = generateMock(User.class);
        final var userId = userRepository.save(entity).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(entity.getName()))
                .andExpect(jsonPath("$.email").value(entity.getEmail()))
                .andExpect(jsonPath("$.password").value(entity.getPassword()))
                .andExpect(jsonPath("$.profiles").isArray());

        userRepository.deleteById(userId);
    }

    @Test
    @DisplayName("Deve lançar uma exceção NotFoundException quando o usuário não for encontrado")
    void testFindByIdWithNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", "123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado. ID: 123 Tipo: UserResponse"))
                .andExpect(jsonPath("$.error").value(NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.path").value("/api/users/123"))
                .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("Deve retornar uma lista de usuários com sucesso")
    void testFindAllWithSuccess() throws Exception {
        final var entity1 = generateMock(User.class);
        final var entity2 = generateMock(User.class);

        userRepository.saveAll(List.of(entity1, entity2));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[1]").isNotEmpty())
                .andExpect(jsonPath("$[0].profiles").isArray());

        userRepository.deleteAll(List.of(entity1, entity2));
    }

    @Test
    @DisplayName("Deve salvar um usuário com sucesso")
    void testSaveUserWithSuccess() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withEmail(VALID_EMAIL);

        mockMvc.perform(
                post(BASE_URI)
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
        ).andExpect(status().isCreated());

        userRepository.deleteByEmail(VALID_EMAIL);
    }

    @Test
    @DisplayName("Deve lançar uma exceção DataIntegrityViolationException quando o e-mail já existir.")
    void testSaveUserWithConflict() throws Exception {
        final var entity = generateMock(User.class).withEmail(VALID_EMAIL);

        userRepository.save(entity);

        final var request = generateMock(CreateUserRequest.class).withEmail(VALID_EMAIL);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email já cadastrado: " + VALID_EMAIL))
                .andExpect(jsonPath("$.error").value(CONFLICT.getReasonPhrase()))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(CONFLICT.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        userRepository.deleteById(entity.getId());
    }

    @Test
    @DisplayName("Deve lançar uma exceção Bad Request quando o nome estiver vazio")
    void testSaveUserWithNameEmptyThenThrowBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withName("").withEmail(VALID_EMAIL);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='O campo nome deve ter entre 3 e 100 caracteres.')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='O campo nome não pode ser vazio.')]").exists());
    }

    @Test
    @DisplayName("Deve lançar uma exceção Bad Request quando o nome for nulo")
    void testSaveUserWithNameNullThenThrowBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withName(null).withEmail(VALID_EMAIL);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='O campo nome não pode ser vazio.')]").exists());
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando o nome estiver em branco")
    void testSaveUserWithNameContainingOnlySpacesThenThrowBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withName("   ").withEmail(VALID_EMAIL);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='O campo nome não pode ser vazio.')]").exists());
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando o nome tiver menos de três caracteres")
    void testSaveUserWithNameContainingLessThenTreeCharactersThenThrowBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withName("ab").withEmail(VALID_EMAIL);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='O campo nome deve ter entre 3 e 100 caracteres.')]").exists());
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando o e-mail for inválido")
    void testSaveUserWithInvalidEmailThenThrowBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='email' && @.message=='must be a well-formed email address')]").exists());
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando o e-mail estiver vazio")
    void testSaveUserWithNullEmailThenThrowBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withEmail(null);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='email' && @.message=='O campo E-mail não pode ser vazio.')]").exists());
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando o e-mail tiver menos de seis caracteres")
    void testSaveUserWithEmailContainingLessThenSixCharactersThenThrowBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withEmail("a@b.c");

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='email' && @.message=='O campo E-mail deve ter entre 6 e 50 caracteres.')]").exists());
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando a senha estiver vazia")
    void testSaveUserWithNullPasswordThenThrowBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withEmail(VALID_EMAIL).withPassword(null);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='password' && @.message=='O campo senha não pode ser vazio.')]").exists());
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando a senha tiver apenas espaços vazios")
    void testSaveUserWithPasswordContainingOnlySpacesThenThrowBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withEmail(VALID_EMAIL).withPassword("   ");

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='password' && @.message=='O campo senha deve ter entre 6 e 20 caracteres.')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='password' && @.message=='O campo senha não pode ser vazio.')]").exists());
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando a senha tiver menos de seis caracteres")
    void testSaveUserWithPasswordContainingLessThenSixCharactersThenThrowBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withEmail(VALID_EMAIL).withPassword("   ");

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='password' && @.message=='O campo senha deve ter entre 6 e 20 caracteres.')]").exists());
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando o nome tiver menos de três caracteres")
    void testUpdateUserWithNameLessThenThreeCharactersThenThrowBadRequest() throws Exception {
        final var request = generateMock(UpdateUserRequest.class).withName("ab");
        final var VALID_ID = userRepository.save(generateMock(User.class)).getId();

        mockMvc.perform(
                        MockMvcRequestBuilders.put(UPDATE_URI, VALID_ID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/" + VALID_ID))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='O campo nome deve ter entre 3 e 100 caracteres.')]").exists());

        userRepository.deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando o e-mail tiver menos de seis caracteres")
    void testUpdateUserWithEmailLessThenSixCharactersThenThrowBadRequest() throws Exception {
        final var request = generateMock(UpdateUserRequest.class).withEmail("a@b.c");
        final var VALID_ID = userRepository.save(generateMock(User.class)).getId();

        mockMvc.perform(
                        MockMvcRequestBuilders.put(UPDATE_URI, VALID_ID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/" + VALID_ID))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='email' && @.message=='O campo E-mail deve ter entre 6 e 50 caracteres.')]").exists());

        userRepository.deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando o e-mail for formatado incorretamente")
    void testUpdateUserWithEmailFormattedIncorrectlyThenThrowBadRequest() throws Exception {
        final var request = generateMock(UpdateUserRequest.class).withEmail("abc");
        final var VALID_ID = userRepository.save(generateMock(User.class)).getId();

        mockMvc.perform(
                        MockMvcRequestBuilders.put(UPDATE_URI, VALID_ID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/" + VALID_ID))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='email' && @.message=='must be a well-formed email address')]").exists());

        userRepository.deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Deve lançar uma exceção de Bad Request quando a senha tiver menos de seis caracteres")
    void testUpdateUserWithPasswordLessThenSixCharactersThenThrowBadRequest() throws Exception {
        final var request = generateMock(UpdateUserRequest.class).withPassword("12345").withEmail(VALID_EMAIL);
        final var VALID_ID = userRepository.save(generateMock(User.class).withId(null)).getId();

        mockMvc.perform(
                        MockMvcRequestBuilders.put(UPDATE_URI, VALID_ID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/" + VALID_ID))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='password' && @.message=='O campo senha deve ter entre 6 e 20 caracteres.')]").exists());

        userRepository.deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Deve lançar uma exceção NotFound quando o ID não for encontrado")
    void testUpdateUserWithIdNotFoundThenThrowNotFoundException() throws Exception {
        final var request = generateMock(UpdateUserRequest.class).withEmail(VALID_EMAIL);

        mockMvc.perform(
                        MockMvcRequestBuilders.put(UPDATE_URI, 1L)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado. ID: 1 Tipo: UserResponse"))
                .andExpect(jsonPath("$.error").value(NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/1"))
                .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    private String toJson(final Object object) throws Exception {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new Exception("Error converting object to JSON", e);
        }
    }

}