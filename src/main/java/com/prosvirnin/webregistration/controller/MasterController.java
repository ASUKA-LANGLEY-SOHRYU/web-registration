package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.service.category.Category;
import com.prosvirnin.webregistration.model.service.category.CategoryDTO;
import com.prosvirnin.webregistration.model.user.dto.EditClientRequest;
import com.prosvirnin.webregistration.model.user.dto.EditMasterRequest;
import com.prosvirnin.webregistration.model.user.dto.EditResponse;
import com.prosvirnin.webregistration.service.CategoryService;
import com.prosvirnin.webregistration.service.MasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/masters")
@Tag(
        name = "Master controller"
)
public class MasterController {

    private final MasterService masterService;
    private final CategoryService categoryService;

    @Autowired
    public MasterController(MasterService masterService, CategoryService categoryService) {
        this.masterService = masterService;
        this.categoryService = categoryService;
    }

    @Operation(
            description = "Editing a master profile. Values equal to null will be ignored. " +
                    "After changing your email, you will receive an activation on your new email. " +
                    "After activation, you need to authenticate again."
    )
    @PostMapping("/me/edit")
    public ResponseEntity<EditResponse> me (Authentication authentication,
                                            @RequestBody EditMasterRequest editMasterRequest)
    {
        return ResponseEntity.ok(masterService.editMe(authentication, editMasterRequest));
    }

    @Operation(
            description = "Adds a category of services to the master by token"
    )
    @PostMapping("/me/categories")
    public ResponseEntity<String> createCategory(Authentication authentication, @RequestBody CategoryDTO categoryDTO){
        categoryService.save(authentication, categoryDTO.map());
        return ResponseEntity.ok("OK!");
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
