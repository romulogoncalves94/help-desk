package br.com.helpdesk.authserviceapi.services;

import br.com.helpdesk.authserviceapi.security.dtos.UserDetailsDTO;
import br.com.helpdesk.authserviceapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final var entity = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return UserDetailsDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .username(entity.getEmail())
                .password(entity.getPassword())
                .authorities(entity.getProfiles().stream().map(x  -> new SimpleGrantedAuthority(x.getDescription())).collect(Collectors.toSet()))
                .build();
    }

}
