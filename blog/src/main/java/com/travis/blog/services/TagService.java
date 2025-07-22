package com.travis.blog.services;

import com.travis.blog.domain.entities.Tag;

import java.util.List;

public interface TagService
{
  List<Tag> getTags();
}
