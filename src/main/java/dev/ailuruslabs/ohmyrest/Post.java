package dev.ailuruslabs.ohmyrest;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;

@Table("posts")
public record Post(
    @Id Integer id,
    String title,
    String content,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt,
    Integer authorId
) {}
