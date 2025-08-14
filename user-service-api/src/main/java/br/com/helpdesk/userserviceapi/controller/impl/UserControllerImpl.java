package br.com.helpdesk.userserviceapi.controller.impl;

import br.com.helpdesk.userserviceapi.controller.UserController;
import br.com.helpdesk.userserviceapi.entity.User;
import br.com.helpdesk.userserviceapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService service;

    @Override
    public ResponseEntity<User> findById(String id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

}
