package com.travis.blog.services;

import com.travis.blog.domain.entities.Category;

import java.util.List;

public interface CategoryService
{
    List<Category> listCategories();

    Category createCategory(Category category);
}
