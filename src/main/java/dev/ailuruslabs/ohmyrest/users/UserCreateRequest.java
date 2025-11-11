package dev.ailuruslabs.ohmyrest.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank
    @Size(min = 3, max = 32, message = "Username must be 3-32 characters")
    String username,

    @NotBlank
    @Size(min = 4, max = 255, message = "Full name must be 3-255 characters")
    String fullName,

    @NotBlank
    @Size(min = 5, max = 255, message = "Email must be 5-255 characters")
    String email,

    // Sending passwords plaintext from client to server is... giving me the "heebie-jeebies."
    @NotBlank
    @Size(min = 8, max = 100, message = "Password must be 8-100 characters")
    String password
) {
}
