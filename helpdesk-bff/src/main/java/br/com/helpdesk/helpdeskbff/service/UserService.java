package br.com.helpdesk.helpdeskbff.service;

import br.com.helpdesk.helpdeskbff.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFeignClient client;

    public UserResponse findById(final String id) {
        return client.findById(id).getBody();
    }

    public void save(CreateUserRequest request) {
        client.save(request);
    }

    public List<UserResponse> findAll() {
        return client.findAll().getBody();
    }

    public UserResponse update(final String id, final UpdateUserRequest request) {
        return client.update(id, request).getBody();
    }

}
