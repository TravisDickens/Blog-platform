package com.travis.blog.mappers;

import ch.qos.logback.core.model.ComponentModel;
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
public interface CategoryMapper
{
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts)
    {
        if (null == posts)
        {
            return  0;
        }

        return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }
}
