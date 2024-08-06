package com.example.shop.app.services;

import com.example.shop.app.DTO.CategoryDTO;
import com.example.shop.app.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id,CategoryDTO categoryDTO);
    void deleteCategory(Long id);

}
