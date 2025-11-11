package dev.ailuruslabs.ohmyrest.security;

import dev.ailuruslabs.ohmyrest.users.AppUserDetailsService;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
class AuthWebFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    private final AppUserDetailsService appUserDetailsService;

    AuthWebFilter(JwtUtil jwtUtil, AppUserDetailsService appUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return chain.filter(exchange);

        String token = authHeader.substring(7);

        return jwtUtil.getUsernameFromToken(authHeader.replaceFirst("Bearer ", ""))
            .flatMap(appUserDetailsService::findByUsername)
            .map(userDetails -> new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            ))
            .flatMap(auth -> chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
    }
}
