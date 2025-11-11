package dev.ailuruslabs.ohmyrest.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(
    @NotBlank
    @Size(min = 3, max = 32, message = "Username must be 3-32 characters")
    String username,

    @NotBlank
    @Size(min = 8, max = 100, message = "Password must be 8-100 characters")
    String password
) {
}
