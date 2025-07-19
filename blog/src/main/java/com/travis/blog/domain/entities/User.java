package com.travis.blog.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User
{
    // Unique ID for the user (UUID is used for safety and scalability)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Email must be unique and not null
    @Column(nullable = false, unique = true)
    private String email;

    // User's password (will be encrypted when stored)
    @Column(nullable = false)
    private String password;

    // Full name of the user
    @Column(nullable = false)
    private String name;

    // A user can author many posts
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    // Timestamp for when the user was created
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Check equality using key fields
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(email, user.email)
                && Objects.equals(password, user.password)
                && Objects.equals(name, user.name)
                && Objects.equals(createdAt, user.createdAt);
    }

    // Hash code generated from the same fields
    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, createdAt);
    }

    // Automatically set the createdAt timestamp before saving to DB
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
