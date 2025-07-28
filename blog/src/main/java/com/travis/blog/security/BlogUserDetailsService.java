package com.travis.blog.security;

import com.travis.blog.domain.entities.User;
import com.travis.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Custom implementation of UserDetailsService for Spring Security
@RequiredArgsConstructor
public class BlogUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Load user details by email (used during authentication)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Look up user by email in the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found with email"));

        // Wrap the User entity in a custom UserDetails implementation
        return new BlogUserDetails(user);
    }
}
