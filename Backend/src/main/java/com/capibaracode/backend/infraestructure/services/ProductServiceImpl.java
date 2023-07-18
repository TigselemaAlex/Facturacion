package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.ProductRequest;
import com.capibaracode.backend.api.models.responses.CategoryResponse;
import com.capibaracode.backend.api.models.responses.ProductResponse;
import com.capibaracode.backend.api.models.responses.PromotionResponse;
import com.capibaracode.backend.api.models.responses.TaxResponse;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.*;
import com.capibaracode.backend.domain.repositories.*;
import com.capibaracode.backend.infraestructure.abstract_services.IProductService;
import com.capibaracode.backend.util.mappers.CategoryMapper;
import com.capibaracode.backend.util.mappers.ProductMapper;
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
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    private final CustomResponseBuilder responseBuilder;
    private final CategoryRepository categoryRepository;
    private final PromotionRepository promotionRepository;

    private final SupplierRepository supplierRepository;
    private final TaxRepository taxRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CustomResponseBuilder responseBuilder, CategoryRepository categoryRepository, PromotionRepository promotionRepository, SupplierRepository supplierRepository, TaxRepository taxRepository) {
        this.productRepository = productRepository;
        this.responseBuilder = responseBuilder;
        this.categoryRepository = categoryRepository;
        this.promotionRepository = promotionRepository;
        this.supplierRepository = supplierRepository;
        this.taxRepository = taxRepository;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(ProductRequest request) {
        if (productRepository.existsByName(request.getName())){
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El producto con nombre \'"+ request.getName()+"\' ya existe.");
        }
        Product product = ProductMapper.INSTANCE.productFromProductRequest(request);
        CategoryResponse categoryResponse = null;
        if (request.getCategory() != null){
            if (categoryRepository.existsById(request.getCategory())){
                Category category = categoryRepository.findById(request.getCategory()).
                        orElseThrow(()-> new RuntimeException("La categoría con id " + request.getCategory() + " no existe."));
                product.setCategory(category);
                PromotionResponse promotionResponse = null;
                TaxResponse taxResponse = null;
                if (category.getPromotion() != null){
                    promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
                }
                if (category.getTax() != null){
                    taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
                }
                categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
            }
        }
        if(request.getPromotion() != null){
            if(promotionRepository.existsById(request.getPromotion())){
                Promotion promotion = promotionRepository.findById(request.getPromotion()).orElseThrow(()-> new RuntimeException("La promoción con id " + request.getPromotion() + " no existe."));
                product.setPromotion(promotion);
            }
        }else{
            product.setPromotion(null);
        }
        if(request.getTax() != null){
            if(taxRepository.existsById(request.getTax())){
                Tax tax = taxRepository.findById(request.getTax()).orElseThrow(()-> new RuntimeException("El impuesto con id " + request.getTax() + " no existe."));
                product.setTax(tax);
            }
        }else{
            product.setTax(null);
        }
        if(request.getSupplier() != null){
            if (supplierRepository.existsById(request.getSupplier())){
                Supplier supplier = supplierRepository.findById(request.getSupplier()).orElseThrow(()-> new RuntimeException("El proveedor con id "+ request.getSupplier() + " no existe"));
                product.setSupplier(supplier);
            }else {
                product.setSupplier(null);
            }
        }
        PromotionResponse promotionResponseFromProduct = PromotionMapper.INSTANCE.promotionResponseFromPromotion(product.getPromotion());
        TaxResponse taxResponseFromProduct = TaxMapper.INSTANCE.taxResponseFromTax(product.getTax());
        ProductResponse productResponse = ProductMapper.INSTANCE.productResponseFromProduct(productRepository.save(product), categoryResponse, promotionResponseFromProduct, taxResponseFromProduct);
        productResponse.setSupplier(product.getSupplier().getId());
        return responseBuilder.buildResponse(HttpStatus.CREATED, "Producto agregado exitosamente.", productResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponse> productResponseList = productList.stream().
                map(product -> {
                    PromotionResponse promotionResponseFromProduct = PromotionMapper.INSTANCE.promotionResponseFromPromotion(product.getPromotion());
                    TaxResponse taxResponseFromProduct = TaxMapper.INSTANCE.taxResponseFromTax(product.getTax());
                    Category category = product.getCategory();
                    CategoryResponse categoryResponse;
                    if(category != null){
                        PromotionResponse promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
                        TaxResponse taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
                        categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
                    }else{
                        categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, null, null);
                    }
                    ProductResponse productResponse = ProductMapper.INSTANCE.productResponseFromProduct(product, categoryResponse, promotionResponseFromProduct, taxResponseFromProduct);
                    productResponse.setSupplier(product.getSupplier().getId());
                    return productResponse;
                }).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Productos.", productResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, ProductRequest request) {
        Product productToEdit = productRepository.findById(id).orElseThrow(()-> new RuntimeException("El producto con id " + id + " no existe."));
        if (productRepository.existsByNameAndIdNot(request.getName(), productToEdit.getId()))
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El producto con nombre \'"+ request.getName()+"\' ya existe.");
        productToEdit.setCode(request.getCode());
        productToEdit.setName(request.getName());
        productToEdit.setQuantity(request.getQuantity());
        productToEdit.setPrice(request.getPrice());
        productToEdit.setStatus(request.getStatus());
        productToEdit.setMinStock(request.getMinStock());
        productToEdit.setMaxStock(request.getMaxStock());
        CategoryResponse categoryResponse = null;
        if (request.getCategory() != null){
            if (categoryRepository.existsById(request.getCategory())){
                Category category = categoryRepository.findById(request.getCategory()).
                        orElseThrow(()-> new RuntimeException("La categoría con id " + request.getCategory() + " no existe."));
                productToEdit.setCategory(category);
                PromotionResponse promotionResponse = null;
                TaxResponse taxResponse = null;
                if (category.getPromotion() != null){
                    promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
                }
                if (category.getTax() != null){
                    taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
                }
                categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
            }
        }else{
            productToEdit.setCategory(null);
        }
        if(request.getPromotion() != null){
            if(promotionRepository.existsById(request.getPromotion())){
                Promotion promotion = promotionRepository.findById(request.getPromotion()).orElseThrow(()-> new RuntimeException("La promoción con id " + id + " no existe."));
                productToEdit.setPromotion(promotion);
            }
        }else{
            productToEdit.setPromotion(null);
        }
        if(request.getTax() != null){
            if(taxRepository.existsById(request.getTax())){
                Tax tax = taxRepository.findById(request.getTax()).orElseThrow(()-> new RuntimeException("El impuesto con id " + id + " no existe."));
                productToEdit.setTax(tax);
            }
        }else{
            productToEdit.setTax(null);
        }
        if(request.getSupplier() != null){
            if (supplierRepository.existsById(request.getSupplier())){
                Supplier supplier = supplierRepository.findById(request.getSupplier()).orElseThrow(()-> new RuntimeException("El proveedor con id "+ request.getSupplier() + " no existe"));
                productToEdit.setSupplier(supplier);
            }else {
                productToEdit.setSupplier(null);
            }
        }
        PromotionResponse promotionResponseFromProduct = PromotionMapper.INSTANCE.promotionResponseFromPromotion(productToEdit.getPromotion());
        TaxResponse taxResponseFromProduct = TaxMapper.INSTANCE.taxResponseFromTax(productToEdit.getTax());
        ProductResponse productResponse = ProductMapper.INSTANCE.productResponseFromProduct(productRepository.save(productToEdit), categoryResponse, promotionResponseFromProduct, taxResponseFromProduct);
        productResponse.setSupplier(productToEdit.getSupplier().getId());
        return responseBuilder.buildResponse(HttpStatus.OK, "Producto actualizado exitosamente.", productResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findByID(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("El producto con id " + id + " no existe."));
        PromotionResponse promotionResponseFromProduct = PromotionMapper.INSTANCE.promotionResponseFromPromotion(product.getPromotion());
        TaxResponse taxResponseFromProduct = TaxMapper.INSTANCE.taxResponseFromTax(product.getTax());
        Category category = product.getCategory();
        CategoryResponse categoryResponse;
        if(category != null){
            PromotionResponse promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
            TaxResponse taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
            categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
        }else{
            categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, null, null);
        }
        ProductResponse productResponse = ProductMapper.INSTANCE.productResponseFromProduct(product, categoryResponse, promotionResponseFromProduct, taxResponseFromProduct);
        productResponse.setSupplier(product.getSupplier().getId());
        return responseBuilder.buildResponse(HttpStatus.OK, "Producto encontrado exitosamente.", productResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findByIDSupplier(UUID id) {
        if (!supplierRepository.existsById(id)){
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El proveedor con id " + id + " no existe");
        }
        Supplier supplier = supplierRepository.findById(id).orElseThrow(()-> new RuntimeException("El proveedor con id " + id + " no existe"));
        List<Product> productList = productRepository.findAllBySupplier(supplier);
        List<ProductResponse> productResponseList = productList.stream().
                map(product -> {
                    PromotionResponse promotionResponseFromProduct = PromotionMapper.INSTANCE.promotionResponseFromPromotion(product.getPromotion());
                    TaxResponse taxResponseFromProduct = TaxMapper.INSTANCE.taxResponseFromTax(product.getTax());
                    Category category = product.getCategory();
                    CategoryResponse categoryResponse;
                    if(category != null){
                        PromotionResponse promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
                        TaxResponse taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
                        categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
                    }else{
                        categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, null, null);
                    }
                    ProductResponse productResponse = ProductMapper.INSTANCE.productResponseFromProduct(product, categoryResponse, promotionResponseFromProduct, taxResponseFromProduct);
                    productResponse.setSupplier(product.getSupplier().getId());
                    return productResponse;
                }).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Productos por Proveedor.", productResponseList);
    }

}
