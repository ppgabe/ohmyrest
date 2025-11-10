package dev.ailuruslabs.ohmyrest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(

    @NotBlank
    @Size(min = 1, max = 255, message = "Title must have 1-255 characters")
    String title,

    @NotBlank(message = "Content cannot be blank")
    String content) {
}
