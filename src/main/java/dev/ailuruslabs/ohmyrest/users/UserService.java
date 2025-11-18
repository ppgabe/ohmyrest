package dev.ailuruslabs.ohmyrest.users;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

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
                passwordEncoder.encode(request.password()),
                ZonedDateTime.now()
            )
        ).onErrorMap(t -> switch (t) {
            case DuplicateKeyException dke -> new ResponseStatusException(HttpStatus.CONFLICT, t.getMessage());
            default -> t;
        });
    }

    // user authentication somewhere here, maybe?

    public Mono<User> getUser(int id) {
        return userRepository.findById(id);
    }
}
