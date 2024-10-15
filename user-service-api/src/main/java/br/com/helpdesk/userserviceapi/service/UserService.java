package br.com.helpdesk.userserviceapi.service;

import br.com.helpdesk.userserviceapi.entity.User;
import br.com.helpdesk.userserviceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(final String id) {
        return userRepository.findById(id).orElse(null);
    }

}
