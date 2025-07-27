package com.travis.blog.services;

import com.travis.blog.domain.entities.User;

import java.util.UUID;

public interface UserService
{
    User getUserById(UUID id);
}
