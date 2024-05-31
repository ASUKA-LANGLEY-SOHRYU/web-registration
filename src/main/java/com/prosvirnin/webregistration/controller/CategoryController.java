package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.service.Category;
import com.prosvirnin.webregistration.model.service.dto.CategoryDTO;
import com.prosvirnin.webregistration.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/masters")
@Tag(
        name = "Category controller"
)
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            description = "Adds a category of services to the master by token. Returns category id"
    )
    @PostMapping("/me/categories")
    public ResponseEntity<Long> createCategory(Authentication authentication, @RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.ok(categoryService.save(authentication, categoryDTO.map()));
    }

    @Operation(
            description = "List of all categories of master by token services"
    )
    @GetMapping("/me/categories")
    public ResponseEntity<List<Category>> getMyCategories(Authentication authentication){
        return ResponseEntity.ok(categoryService.findAllByAuthentication(authentication));
    }

    @Operation(
            description = "Changing service category"
    )
    @PutMapping("/me/categories/{id}")
    public ResponseEntity<String> editMyCategories(Authentication authentication,
                                                   @PathVariable("id") Long id,
                                                   @RequestBody CategoryDTO edited){
        if (categoryService.edit(authentication, id, edited))
            return ResponseEntity.ok("OK!");
        return ResponseEntity.ok("ERROR!");
    }

    @Operation(
            description = "Removing a service category"
    )
    @DeleteMapping("/me/categories/{id}")
    public ResponseEntity<String> deleteMyCategories(Authentication authentication,
                                                     @PathVariable("id") Long id){
        if (categoryService.delete(authentication, id))
            return ResponseEntity.ok("OK!");
        return ResponseEntity.ok("ERROR!");
    }

    @Operation(
            description = "List of all categories of master with id = <id> services"
    )
    @GetMapping("/{id}/categories")
    public ResponseEntity<List<Category>> getCategories(@PathVariable("id") Long id){
        return ResponseEntity.ok(categoryService.findAllByMasterId(id));
    }
}
