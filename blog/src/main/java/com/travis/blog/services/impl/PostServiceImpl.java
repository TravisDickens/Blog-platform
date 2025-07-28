package com.travis.blog.services.impl;

import com.travis.blog.domain.CreatePostRequest;
import com.travis.blog.domain.PostStatus;
import com.travis.blog.domain.UpdatePostRequest;
import com.travis.blog.domain.entities.Category;
import com.travis.blog.domain.entities.Post;
import com.travis.blog.domain.entities.Tag;
import com.travis.blog.domain.entities.User;
import com.travis.blog.repositories.PostRepository;
import com.travis.blog.services.CategoryService;
import com.travis.blog.services.PostService;
import com.travis.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;

    @Override
    public Post getPost(UUID id) {
        // Fetch post by ID, or throw if not found
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with that id"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        // Filter by both category and tag
        if (categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        }

        // Filter by category only
        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        // Filter by tag only
        if (tagId != null) {
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }

        // No filters — return all published posts
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        // Fetch all drafts authored by the user
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);

        // Calculate reading time based on word count
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        // Set category
        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        // Convert tag IDs to tag entities
        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagsByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with that id"));

        // Update content and status
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        // Update category only if it has changed
        UUID newCategoryId = updatePostRequest.getCategoryId();
        if (!existingPost.getCategory().getId().equals(newCategoryId)) {
            Category newCategory = categoryService.getCategoryById(newCategoryId);
            existingPost.setCategory(newCategory);
        }

        // Compare and update tags only if different
        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> requestTagIds = updatePostRequest.getTagIds();

        if (!existingTagIds.equals(requestTagIds)) {
            List<Tag> newTags = tagService.getTagsByIds(requestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(UUID id) {
        // Delete post by ID
        Post post = getPost(id);
        postRepository.delete(post);
    }

    /**
     * Estimates reading time based on word count.
     * Assumes average reading speed of 200 words per minute.
     */
    private Integer calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    }
}
