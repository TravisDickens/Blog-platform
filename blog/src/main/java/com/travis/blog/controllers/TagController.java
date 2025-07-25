package com.travis.blog.controllers;

import com.travis.blog.domain.dto.CreateTagsRequest;
import com.travis.blog.domain.dto.TagResponse;
import com.travis.blog.domain.entities.Tag;
import com.travis.blog.mappers.TagMapper;
import com.travis.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@RequestBody CreateTagsRequest createTagsRequest)
    {
       List<Tag> savedTags = tagService.createTags(createTagsRequest.getNames());

        List<TagResponse> createdTagResponses = savedTags.stream().map(tag -> tagMapper.toTagResponse(tag)).toList();

        return new ResponseEntity<>(createdTagResponses, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id)
    {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

}
