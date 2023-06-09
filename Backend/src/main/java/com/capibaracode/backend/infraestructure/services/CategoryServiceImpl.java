package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.CategoryRequest;
import com.capibaracode.backend.api.models.responses.CategoryResponse;
import com.capibaracode.backend.api.models.responses.PromotionResponse;
import com.capibaracode.backend.api.models.responses.TaxResponse;
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
import com.capibaracode.backend.util.mappers.PromotionMapper;
import com.capibaracode.backend.util.mappers.TaxMapper;
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
        if (categoryRepository.existsByCategory(request.getCategory())){
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "La categoría con nombre \'"+ request.getCategory()+"\' ya existe.");
        }
        Category category = CategoryMapper.INSTANCE.categoryFromCategoryRequest(request);
        CategoryResponse categoryResponse = new CategoryResponse();
        PromotionResponse promotionResponse = new PromotionResponse();
        TaxResponse taxResponse = new TaxResponse();
        if (request.getPromotion() != null){
            if (promotionRepository.existsById(request.getPromotion())){
                Promotion promotion = promotionRepository.findById(request.getPromotion()).
                        orElseThrow(()-> new RuntimeException("La promocion con id " + request.getPromotion() + " no existe."));
                category.setPromotion(promotion);
                promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(promotion);
            }
        }
        if (request.getTax() != null){
            if (taxRepository.existsById(request.getTax())){
                Tax tax = taxRepository.findById(request.getTax()).
                        orElseThrow(()-> new RuntimeException("El impuesto con id " + request.getTax()+ " no existe."));
                category.setTax(tax);
                taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(tax);
            }
        }
        categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(categoryRepository.save(category), promotionResponse, taxResponse);

        return responseBuilder.buildResponse(HttpStatus.CREATED, "Categoría agregada exitosamente", categoryResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryResponse> categoryResponseList =
               categoryList.stream()
                        .map(category -> {
                            var promotion = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
                            var tax = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
                            return CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotion, tax);
                        }).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Categorias", categoryResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, CategoryRequest request) {
        Category categoryToEdit = categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("La categoria con id " + id + " no existe."));
        boolean categoryExists = categoryRepository.existsByCategoryAndIdNot(request.getCategory(), id);
        if (categoryExists){
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "La categoría con nombre \'"+ request.getCategory()+"\' ya existe.");
        }
        CategoryResponse categoryResponse = new CategoryResponse();
        PromotionResponse promotionResponse = null;
        TaxResponse taxResponse = null;
        categoryToEdit.setCategory(request.getCategory());
        categoryToEdit.setStatus(request.getStatus());
        if (request.getPromotion() != null){
            if (promotionRepository.existsById(request.getPromotion())){
                Promotion promotion = promotionRepository.findById(request.getPromotion()).
                        orElseThrow(()-> new RuntimeException("La promocion con id " + request.getPromotion() + " no existe."));
                categoryToEdit.setPromotion(promotion);
                promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(promotion);
            }
        }else{
            categoryToEdit.setPromotion(null);
        }
        if (request.getTax() != null){
            if (taxRepository.existsById(request.getTax())){
                Tax tax = taxRepository.findById(request.getTax()).
                        orElseThrow(()-> new RuntimeException("El impuesto con id " + request.getTax()+ " no existe."));
                categoryToEdit.setTax(tax);
                taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(tax);
            }
        }else{
            categoryToEdit.setTax(null);
        }

        categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(categoryRepository.save(categoryToEdit), promotionResponse, taxResponse);

        return responseBuilder.buildResponse(HttpStatus.OK, "Categoria actualizada exitosamente", categoryResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findByID(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("La categoría con id " + id + " no existe."));
        PromotionResponse promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
        TaxResponse taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
        CategoryResponse categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
        return responseBuilder.buildResponse(HttpStatus.OK, "Categoría Encontrada", categoryResponse);
    }

}
