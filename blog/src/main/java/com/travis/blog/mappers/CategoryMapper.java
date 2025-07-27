package com.travis.blog.mappers;

import com.travis.blog.domain.PostStatus;
import com.travis.blog.domain.dto.CategoryDto;
import com.travis.blog.domain.dto.CreateCategoryRequest;
import com.travis.blog.domain.entities.Category;
import com.travis.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    // Converts a Category entity to CategoryDto
    // Maps the list of posts to a postCount field (only counts published ones)
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    // Converts a CreateCategoryRequest DTO to a Category entity
    Category toEntity(CreateCategoryRequest createCategoryRequest);

    // Custom method to count only published posts in a category
    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (posts == null) {
            return 0;
        }

        // Only count posts that are published
        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
