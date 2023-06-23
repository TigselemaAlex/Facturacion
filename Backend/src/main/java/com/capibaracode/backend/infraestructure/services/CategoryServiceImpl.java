package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.CategoryRequest;
import com.capibaracode.backend.api.models.responses.CategoryResponse;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.Category;
import com.capibaracode.backend.domain.entities.Promotion;
import com.capibaracode.backend.domain.entities.Tax;
import com.capibaracode.backend.domain.repositories.CategoryRepository;
import com.capibaracode.backend.domain.repositories.PromotionRepository;
import com.capibaracode.backend.domain.repositories.TaxRepository;
import com.capibaracode.backend.infraestructure.abstract_services.ICategoryService;
import com.capibaracode.backend.util.mappers.CategoryMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final PromotionRepository promotionRepository;
    private final TaxRepository taxRepository;
    private final CustomResponseBuilder responseBuilder;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, PromotionRepository promotionRepository, TaxRepository taxRepository, CustomResponseBuilder responseBuilder) {
        this.categoryRepository = categoryRepository;
        this.promotionRepository = promotionRepository;
        this.taxRepository = taxRepository;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(CategoryRequest request) {
        Category category = CategoryMapper.INSTANCE.categoryFromCategoryRequest(request);
        CategoryResponse categoryResponse = new CategoryResponse();
        if (request.getPromotionId() != null){
            if (promotionRepository.existsById(request.getPromotionId())){
                Promotion promotion = promotionRepository.findById(request.getPromotionId()).
                        orElseThrow(()-> new RuntimeException("La promocion con id " + request.getPromotionId() + " no existe."));
                category.setPromotion(promotion);
            }
        }
        if (request.getTaxId() != null){
            if (taxRepository.existsById(request.getTaxId())){
                Tax tax = taxRepository.findById(request.getTaxId()).
                        orElseThrow(()-> new RuntimeException("El impuesto con id " + request.getTaxId()+ " no existe."));
                category.setTax(tax);
            }
        }
        categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(categoryRepository.save(category));

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
        Category categoryToEdit = categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("La categoria con id " + id + "no existe."));
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryToEdit.setCategory(request.getCategory());
        categoryToEdit.setStatus(request.getStatus());
        if (request.getPromotionId() != null){
            if (promotionRepository.existsById(request.getPromotionId())){
                Promotion promotion = promotionRepository.findById(request.getPromotionId()).
                        orElseThrow(()-> new RuntimeException("La promocion con id " + request.getPromotionId() + " no existe."));
                categoryToEdit.setPromotion(promotion);

            }
        }
        if (request.getTaxId() != null){
            if (taxRepository.existsById(request.getTaxId())){
                Tax tax = taxRepository.findById(request.getTaxId()).
                        orElseThrow(()-> new RuntimeException("El impuesto con id " + request.getTaxId()+ " no existe."));
                categoryToEdit.setTax(tax);

            }
        }
        categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(categoryRepository.save(categoryToEdit));
        return responseBuilder.buildResponse(HttpStatus.OK, "Categoria actualizada exitosamente", categoryResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findByNameCategory(String categoryName) {
        Category category = categoryRepository.findByCategory(categoryName).orElseThrow(()-> new RuntimeException(categoryName+ " no existe"));
        CategoryResponse categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category);
        return responseBuilder.buildResponse(HttpStatus.OK, "Categoría Encontrada", categoryResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> delete(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return responseBuilder.buildResponse(HttpStatus.OK, "Categoria removida exitosamente");
        }
        throw  new RuntimeException("La categoria con el identificador: " + id + " no se encuentra.");
    }
}
