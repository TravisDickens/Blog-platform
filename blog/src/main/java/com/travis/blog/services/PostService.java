package com.travis.blog.services;

import com.travis.blog.domain.entities.Post;

import java.util.List;
import java.util.UUID;

public interface PostService
{
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
}
