package com.travis.blog.domain.entities;

import com.travis.blog.domain.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Post
{
    // Unique identifier for the post (UUID)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Title of the post (can't be null)
    @Column(nullable = false)
    private String title;

    // Main content of the post, stored as TEXT in the DB
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Enum that tracks whether the post is published or in draft
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    // Estimated reading time in minutes
    @Column(nullable = false)
    private Integer readingTime;

    // The user who wrote the post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Category assigned to this post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Tags associated with this post (many-to-many relationship)
    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Timestamp for when the post was created
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Timestamp for when the post was last updated
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // equals to compare posts by key fields
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id)
                && Objects.equals(title, post.title)
                && Objects.equals(content, post.content)
                && status == post.status
                && Objects.equals(readingTime, post.readingTime)
                && Objects.equals(createdAt, post.createdAt)
                && Objects.equals(updatedAt, post.updatedAt);
    }

    // Hash code also based on key fields
    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, status, readingTime, createdAt, updatedAt);
    }

    // Automatically set timestamps before the post is saved the first time
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // Update the 'updatedAt' timestamp whenever the post is modified
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
