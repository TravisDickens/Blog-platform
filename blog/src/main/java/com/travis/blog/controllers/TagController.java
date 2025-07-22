package com.travis.blog.controllers;

import com.travis.blog.domain.dto.TagResponse;
import com.travis.blog.domain.entities.Tag;
import com.travis.blog.mappers.TagMapper;
import com.travis.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController
{
    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags()
    {
      List<Tag> tags = tagService.getTags();
      List<TagResponse> tagResponses = tags.stream().map(tagMapper::toTagResponse).toList();
      return ResponseEntity.ok(tagResponses);
    }
}
