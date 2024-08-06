package com.example.shop.app.controller;

import com.example.shop.app.DTO.CategoryDTO;
import com.example.shop.app.models.Category;
import com.example.shop.app.services.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
//@Validated
@RequiredArgsConstructor
public class CategoryController {
    //Hiển thị tất cả các categories
    //    http://localhost:8088/api/v1/categories?page=1&limit=10
    private final CategoryService categoryService;
    @PostMapping("")
    public ResponseEntity<?> createCategories(@Valid @RequestBody CategoryDTO categoryDTO,
                                              BindingResult result){
        if (result.hasErrors()){
            List<String> errorMessages= result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("insert category successfully");
    }
    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
            ){
        List<Category> categories= categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategories(@PathVariable long id,@RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("update Categories successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("delete Category with id "+id+" successfully");
    }
}
