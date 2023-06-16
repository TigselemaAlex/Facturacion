package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.CategoryRequest;
import com.capibaracode.backend.api.models.responses.CategoryResponse;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.ResponseBuilder;
import com.capibaracode.backend.domain.entities.Category;
import com.capibaracode.backend.domain.repositories.CategoryRepository;
import com.capibaracode.backend.infraestructure.abstract_services.ICategoryService;
import com.capibaracode.backend.util.mappers.CategoryMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ResponseBuilder responseBuilder) {
        this.categoryRepository = categoryRepository;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(CategoryRequest request) {
        Category category = CategoryMapper.INSTANCE.categoryFromCategoryRequest(request);
        CategoryResponse categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(categoryRepository.save(category));
        return responseBuilder.buildResponse(HttpStatus.CREATED, "Categoría agregada exitosamente", categoryResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryResponse> categoryResponseList =
                categoryList.stream()
                        .map(CategoryMapper.INSTANCE::categoryResponseFromCategory).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Categorias", categoryResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, CategoryRequest request) {
        Category categoryToEdit = categoryRepository.findById(id).orElseThrow();
        categoryToEdit.setCategory(request.getCategory());
        categoryToEdit.setStatus(request.getStatus());
        categoryToEdit.setPromotion(request.getPromotion());
        categoryToEdit.setTax(request.getTax());
        CategoryResponse categoryRequest = CategoryMapper.INSTANCE.categoryResponseFromCategory(categoryToEdit);
        return responseBuilder.buildResponse(HttpStatus.OK, "Categoria actualizada exitosamente", categoryRequest);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findByNameCategory(String categoryName) {
        Category category = categoryRepository.findByCategory(categoryName);
        CategoryResponse categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category);
        return responseBuilder.buildResponse(HttpStatus.OK, "Categoría Encontrada", categoryResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> delete(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return responseBuilder.buildResponse(HttpStatus.OK, "Categoria removida exitosamente");
        }
        return null;
        //throw new CategoryNotFoundException(id);
    }
}
