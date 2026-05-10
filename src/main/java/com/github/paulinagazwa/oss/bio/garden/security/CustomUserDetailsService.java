package com.github.paulinagazwa.oss.bio.garden.security;

import com.github.paulinagazwa.oss.bio.garden.entity.UserEntity;
import com.github.paulinagazwa.oss.bio.garden.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity user = userRepository
                .findByEmailIgnoreCaseOrUsernameIgnoreCase(login, login)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found for login: " + login
                ));

        return new GardenUserDetails(user);
    }
}
