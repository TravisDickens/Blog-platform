package com.travis.blog.controllers;

import com.travis.blog.domain.dto.CategoryDto;
import com.travis.blog.domain.dto.CreateCategoryRequest;
import com.travis.blog.domain.entities.Category;
import com.travis.blog.mappers.CategoryMapper;
import com.travis.blog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor // Injects service and mapper through constructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    // Get all categories as DTOs
    @GetMapping()
    public ResponseEntity<List<CategoryDto>> listCategories() {
        List<CategoryDto> categories = categoryService.listCategories()
                .stream()
                .map(categoryMapper::toDto) // convert each Category to DTO
                .toList();

        return ResponseEntity.ok(categories);
    }

    // Create a new category from request body
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        // Convert request DTO to entity
        Category categoryToCreate = categoryMapper.toEntity(createCategoryRequest);

        // Save it using the service
        Category savedCategory = categoryService.createCategory(categoryToCreate);

        // Return the created category as DTO
        return new ResponseEntity<>(categoryMapper.toDto(savedCategory), HttpStatus.CREATED);
    }

    // Delete a category by its ID
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204: successfully deleted
    }
}
