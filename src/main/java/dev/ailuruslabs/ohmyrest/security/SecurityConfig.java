package dev.ailuruslabs.ohmyrest.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@ConfigurationProperties(prefix = "security-config")
@EnableWebFluxSecurity
class SecurityConfig {

    private Argon2Config argon2Config;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(authorize -> authorize
                .pathMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                .anyExchange().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(
            argon2Config.saltLength,
            argon2Config.hashLength,
            argon2Config.parallelism,
            argon2Config.memory,
            argon2Config.iterations
        );
    }

    public Argon2Config getArgon2Config() {
        return argon2Config;
    }

    public void setArgon2Config(Argon2Config argon2Config) {
        this.argon2Config = argon2Config;
    }

    static class Argon2Config {
        private int saltLength;
        private int hashLength;
        private int parallelism;
        private int memory;
        private int iterations;

        public int getSaltLength() {
            return saltLength;
        }

        public void setSaltLength(int saltLength) {
            this.saltLength = saltLength;
        }

        public int getHashLength() {
            return hashLength;
        }

        public void setHashLength(int hashLength) {
            this.hashLength = hashLength;
        }

        public int getParallelism() {
            return parallelism;
        }

        public void setParallelism(int parallelism) {
            this.parallelism = parallelism;
        }

        public int getMemory() {
            return memory;
        }

        public void setMemory(int memory) {
            this.memory = memory;
        }

        public int getIterations() {
            return iterations;
        }

        public void setIterations(int iterations) {
            this.iterations = iterations;
        }
    }
}
