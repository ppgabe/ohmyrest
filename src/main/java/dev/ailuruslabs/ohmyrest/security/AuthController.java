package dev.ailuruslabs.ohmyrest.security;

import dev.ailuruslabs.ohmyrest.users.UserCreateRequest;
import dev.ailuruslabs.ohmyrest.users.UserLoginRequest;
import dev.ailuruslabs.ohmyrest.users.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
class AuthController {

    private final UserService userService;
    private final ReactiveAuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    AuthController(UserService userService, ReactiveAuthenticationManager reactiveAuthenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authManager = reactiveAuthenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<AuthResponse>> registerUser(@Valid @RequestBody Mono<UserCreateRequest> userCreateRequestMono) {
        return userCreateRequestMono.flatMap(userService::saveUser)
            .map(
                user -> new UsernamePasswordAuthenticationToken(user.username(), null, List.of())
            )
            .flatMap(jwtUtil::generateToken)
            .map(jwtString -> ResponseEntity.ok().body(new AuthResponse(jwtString)));
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> loginUser(@Valid @RequestBody Mono<UserLoginRequest> userLoginRequestMono) {
        return userLoginRequestMono.map(
                userLoginRequest -> new UsernamePasswordAuthenticationToken(
                    userLoginRequest.username(),
                    userLoginRequest.password()
                )
            )
            .flatMap(authManager::authenticate)
            .flatMap(jwtUtil::generateToken)
            .map(jwtString -> ResponseEntity.ok().body(new AuthResponse(jwtString)));
    }
}
