package dev.ailuruslabs.ohmyrest.users;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Service
public class AppUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findUserByUsername(username)
            .map(
                user -> User.builder()
                    .username(user.username())
                    .password(user.hashedPassword())
                    .build()
            )
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("Username " + username + " not found.")));
    }
}
