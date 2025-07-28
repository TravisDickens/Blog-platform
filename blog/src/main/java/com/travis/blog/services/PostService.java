package com.travis.blog.services;

import com.travis.blog.domain.CreatePostRequest;
import com.travis.blog.domain.UpdatePostRequest;
import com.travis.blog.domain.entities.Post;
import com.travis.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService
{
    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    void deletePost(UUID id);
}
