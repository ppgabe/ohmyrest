package dev.ailuruslabs.ohmyrest.errors;

public record ErrorResponse(int statusCode, String message, long timestamp) {
}

