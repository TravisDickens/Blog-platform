package com.travis.blog.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Tag
{
    // Primary key for the tag, generated as a UUID
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Tag name must be unique and not null
    @Column(nullable = false, unique = true)
    private String name;

    // Many-to-many relationship with posts, mapped by the "tags" field in the Post entity
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    // Equality check based on id and name
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }

    // Hash code also based on id and name
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
