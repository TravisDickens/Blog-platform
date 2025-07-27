package com.travis.blog.config;

import com.travis.blog.domain.entities.User;
import com.travis.blog.repositories.UserRepository;
import com.travis.blog.security.BlogUserDetailsService;
import com.travis.blog.security.JwtAuthenticationFilter;
import com.travis.blog.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // Creating the custom JWT filter and injecting the auth service it needs
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService) {
        return new JwtAuthenticationFilter(authenticationService);
    }

    // Set up the user details service, responsible for loading user data during authentication
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        BlogUserDetailsService blogUserDetailsService = new BlogUserDetailsService(userRepository);

        // For testing/demo purposes: create a default user if it doesn't already exist
        String email = "user@test.com";

        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .name("Test User")
                    .email(email)
                    .password(passwordEncoder().encode("password123!")) // default password, encoded
                    .build();

            return userRepository.save(newUser);
        });

        return blogUserDetailsService;
    }

    // This configures which endpoints require auth and which ones are public
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        // Allow anyone to log in
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        // Only logged-in users can view draft posts
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/drafts").authenticated()
                        // Public access for viewing posts, categories, and tags
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll()
                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                // Disable CSRF protection since this is a stateless API
                .csrf(csrf -> csrf.disable())
                // Don't use sessions — rely completely on JWTs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Register our JWT filter to run before Spring Security's built-in one
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Spring's default password encoder factory — safe to use for now
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Expose the authentication manager so it can be injected into other components
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
