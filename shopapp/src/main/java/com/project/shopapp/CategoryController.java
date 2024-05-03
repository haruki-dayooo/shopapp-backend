package com.project.shopapp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
    //Show all categories
    @GetMapping("")
    public ResponseEntity<String> getAllCategories() {
        return ResponseEntity.ok("Get all categories");
    }

    @PostMapping("")
    public ResponseEntity<String> insertCategories() {
        return ResponseEntity.ok("Insert category");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateCategories(@PathVariable Long id) {
        return ResponseEntity.ok("Update category with ID: " + id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable Long id) {
        return ResponseEntity.ok("Delete category with ID: " + id);
    }
}
