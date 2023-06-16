package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.CategoryRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.ICategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/category")
//http://localhost:8090/capy-bills/api/category
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CustomAPIResponse<?>> save(@RequestBody final CategoryRequest request){
        return categoryService.save(request);
    }

    @GetMapping
    public ResponseEntity<CustomAPIResponse<?>> getAll(){
        return categoryService.getAll();
    }

    @GetMapping(value = "/{category}")
    public ResponseEntity<CustomAPIResponse<?>> getByCategory(String category){
        return categoryService.findByNameCategory(category);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> update(@PathVariable final UUID id, @RequestBody final CategoryRequest request){
        return categoryService.update(id, request);
    }

    @DeleteMapping
    public ResponseEntity<CustomAPIResponse<?>> delete(@PathVariable final UUID id){
        return categoryService.delete(id);
    }

}
