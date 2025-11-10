package dev.ailuruslabs.ohmyrest;

public record ErrorResponse(int statusCode, String message, long timestamp) {
}
