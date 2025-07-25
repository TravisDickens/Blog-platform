package com.travis.blog.services.impl;

import com.travis.blog.domain.entities.Post;
import com.travis.blog.repositories.PostRepository;
import com.travis.blog.services.CategoryService;
import com.travis.blog.services.PostService;
import com.travis.blog.services.TagService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService
{

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;


    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {

       if(categoryId != null && tagId !=null){

       }
    }
}
