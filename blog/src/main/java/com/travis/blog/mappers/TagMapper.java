package com.travis.blog.mappers;

import com.travis.blog.domain.PostStatus;
import com.travis.blog.domain.dto.TagResponse;
import com.travis.blog.domain.entities.Post;
import com.travis.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    // Converts a Tag entity to a TagResponse DTO
    // Automatically calculates the number of published posts for the tag
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagResponse toTagResponse(Tag tag);

    // Custom logic to count only published posts in a tag's post set
    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts) {
        if (posts == null) {
            return 0;
        }

        return (int) posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
