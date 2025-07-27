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
@RequiredArgsConstructor // handles constructor injection for service + mapper
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    // Get all tags (used to display available tags for filtering or assigning)
    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<Tag> tags = tagService.getTags();

        // Convert to response DTOs before returning
        List<TagResponse> tagResponses = tags.stream()
                .map(tagMapper::toTagResponse)
                .toList();

        return ResponseEntity.ok(tagResponses);
    }

    // Create multiple tags from a list of names (batch insert)
    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@RequestBody CreateTagsRequest createTagsRequest) {
        // Save tags from the request body
        List<Tag> savedTags = tagService.createTags(createTagsRequest.getNames());

        // Convert saved entities to response DTOs
        List<TagResponse> createdTagResponses = savedTags.stream()
                .map(tagMapper::toTagResponse)
                .toList();

        return new ResponseEntity<>(createdTagResponses, HttpStatus.CREATED); // 201 response
    }

    // Delete a tag by ID
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
