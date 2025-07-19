package com.travis.blog.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category
{
    // Unique ID for each category, generated as a UUID
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Category name must be unique and not null
    @Column(nullable = false, unique = true)
    private String name;

    // One category can have many posts
    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();

    // Compare categories by id and name
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name);
    }

    // Hash code based on id and name
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
