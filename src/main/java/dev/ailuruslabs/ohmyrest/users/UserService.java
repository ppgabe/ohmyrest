package dev.ailuruslabs.ohmyrest.users;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository repo, PasswordEncoder pe) {
        this.userRepository = repo;
        this.passwordEncoder = pe;
    }

    public Mono<User> saveUser(UserCreateRequest request) {
        return userRepository.save(
            new User(
                null,
                request.username(),
                request.fullName(),
                request.email(),

                // Not sure if this is the best place for this responsibility.
                passwordEncoder.encode(request.password())
                )
        );
    }

    // user authentication somewhere here, maybe?

    public Mono<User> getUser(int id) {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found.")
            ));
    }
}
