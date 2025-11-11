package dev.ailuruslabs.ohmyrest.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record User(Integer id, String username, String fullName, String email, String hashedPassword) {

}
