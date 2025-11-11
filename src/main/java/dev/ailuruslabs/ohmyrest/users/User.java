package dev.ailuruslabs.ohmyrest.users;

import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;

@Table("users")
public record User(Integer id, String username, String fullName, String email, String hashedPassword,
                   ZonedDateTime createdAt) {

}
