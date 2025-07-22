package com.travis.blog.services.impl;

import com.travis.blog.domain.entities.Tag;
import com.travis.blog.repositories.TagRepository;
import com.travis.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService
{
    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags()
    {
        return tagRepository.findAllWithPostCount();

    }
}
