package dev.ailuruslabs.ohmyrest.errors;

import java.util.List;

public record ErrorResponseWithFields(int statusCode, List<FieldAndMessage> fieldErrors, long timestamp) {}