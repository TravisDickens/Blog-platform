package com.travis.blog.controllers;

import com.travis.blog.domain.CreatePostRequest;
import com.travis.blog.domain.UpdatePostRequest;
import com.travis.blog.domain.dto.CreatePostRequestDto;
import com.travis.blog.domain.dto.PostDto;
import com.travis.blog.domain.dto.UpdatePostRequestDto;
import com.travis.blog.domain.entities.Post;
import com.travis.blog.domain.entities.User;
import com.travis.blog.mappers.PostMapper;
import com.travis.blog.services.PostService;
import com.travis.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor // injects the services automatically via constructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    // Get all published posts
    // If categoryId or tagId provided, filter by those
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId) {

        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream()
                .map(postMapper::toDto)
                .toList();

        return ResponseEntity.ok(postDtos);
    }

    // Get all draft posts for logged in user (userId injected from filter)
    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById(userId);

        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        List<PostDto> postDtos = draftPosts.stream()
                .map(postMapper::toDto)
                .toList();

        return ResponseEntity.ok(postDtos);
    }

    // Create a new post for current user
    @PostMapping
    public ResponseEntity<PostDto> createPosts(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId) {

        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);

        Post createdPost = postService.createPost(loggedInUser, createPostRequest);
        PostDto createdPostDto = postMapper.toDto(createdPost);

        // Return created post with 201 status
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    // Update post by id
    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {

        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        PostDto updatedPostDto = postMapper.toDto(updatedPost);

        return ResponseEntity.ok(updatedPostDto);
    }

    // Get post by id
    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id) {
        Post post = postService.getPost(id);
        PostDto postDto = postMapper.toDto(post);

        return ResponseEntity.ok(postDto);
    }

    // Delete post by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);

        // No content response on successful delete
        return ResponseEntity.noContent().build();
    }
}
