package com.travis.blog.mappers;

import com.travis.blog.domain.CreatePostRequest;
import com.travis.blog.domain.UpdatePostRequest;
import com.travis.blog.domain.dto.CreatePostRequestDto;
import com.travis.blog.domain.dto.PostDto;
import com.travis.blog.domain.dto.UpdatePostRequestDto;
import com.travis.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    // Maps a Post entity to a PostDto
    // Explicitly maps author, category, and tags (in case nested DTOs are needed)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto dto);
}
