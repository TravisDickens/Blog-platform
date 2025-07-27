package com.travis.blog.controllers;

import com.travis.blog.domain.CreatePostRequest;
import com.travis.blog.domain.dto.CreatePostRequestDto;
import com.travis.blog.domain.dto.PostDto;
import com.travis.blog.domain.entities.Post;
import com.travis.blog.domain.entities.User;
import com.travis.blog.mappers.PostMapper;
import com.travis.blog.services.PostService;
import com.travis.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor // injects required services via constructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    // Get all published posts
    // Supports optional filtering by categoryId or tagId
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

    // Get all draft posts for the logged-in user
    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById(userId);

        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        List<PostDto> postDtos = draftPosts.stream()
                .map(postMapper::toDto)
                .toList();

        return ResponseEntity.ok(postDtos);
    }

    // Create a new post as the currently authenticated user
    @PostMapping
    public ResponseEntity<PostDto> createPosts(@RequestBody CreatePostRequestDto createPostRequestDto, @RequestAttribute UUID userId)
    {
        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);

       Post createdPosts = postService.createPost(loggedInUser, createPostRequest);

       PostDto createdPostDto = postMapper.toDto(createdPosts);

       return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);

    }
}
