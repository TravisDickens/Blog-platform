package com.travis.blog.controllers;

import com.travis.blog.domain.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController
{
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false)UUID categoryId, @RequestParam(required = false)UUID tagId)
    {

    }
}
