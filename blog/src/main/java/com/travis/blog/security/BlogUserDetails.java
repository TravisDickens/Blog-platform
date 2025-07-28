package com.travis.blog.security;

import com.travis.blog.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor // injects the User via constructor
public class BlogUserDetails implements UserDetails {

    private final User user;

    // Return user roles/authorities — right now, every user is assigned ROLE_USER
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // Used by Spring Security for authentication
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // This is the unique identifier Spring Security uses — in our case, it's the user's email
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // Account is always considered non-expired for now
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Account is not locked — can enhance this later
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Credentials (password) are not expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Account is enabled — can add logic later to disable users
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Expose the user’s UUID — useful when extracting from security context
    public UUID getId() {
        return user.getId();
    }
}
