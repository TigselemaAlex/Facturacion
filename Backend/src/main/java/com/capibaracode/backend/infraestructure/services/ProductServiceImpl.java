package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.ProductRequest;
import com.capibaracode.backend.api.models.responses.CategoryResponse;
import com.capibaracode.backend.api.models.responses.ProductResponse;
import com.capibaracode.backend.api.models.responses.PromotionResponse;
import com.capibaracode.backend.api.models.responses.TaxResponse;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.Category;
import com.capibaracode.backend.domain.entities.Product;
import com.capibaracode.backend.domain.entities.Promotion;
import com.capibaracode.backend.domain.entities.Tax;
import com.capibaracode.backend.domain.repositories.CategoryRepository;
import com.capibaracode.backend.domain.repositories.ProductRepository;
import com.capibaracode.backend.domain.repositories.PromotionRepository;
import com.capibaracode.backend.domain.repositories.TaxRepository;
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
    private final TaxRepository taxRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CustomResponseBuilder responseBuilder, CategoryRepository categoryRepository, PromotionRepository promotionRepository, TaxRepository taxRepository) {
        this.productRepository = productRepository;
        this.responseBuilder = responseBuilder;
        this.categoryRepository = categoryRepository;
        this.promotionRepository = promotionRepository;
        this.taxRepository = taxRepository;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(ProductRequest request) {
        if (productRepository.existsByCode(request.getCode())){
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El producto con código \'"+ request.getCode()+"\' ya existe.");
        }
        Product product = ProductMapper.INSTANCE.productFromProductRequest(request);
        CategoryResponse categoryResponse = new CategoryResponse();
        if (request.getCategory() != null){
            if (categoryRepository.existsById(request.getCategory())){
                Category category = categoryRepository.findById(request.getCategory()).
                        orElseThrow(()-> new RuntimeException("La categoría con id " + request.getCategory() + " no existe."));
                product.setCategory(category);
                PromotionResponse promotionResponse = new PromotionResponse();
                TaxResponse taxResponse = new TaxResponse();
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
        PromotionResponse promotionResponseFromProduct = PromotionMapper.INSTANCE.promotionResponseFromPromotion(product.getPromotion());
        TaxResponse taxResponseFromProduct = TaxMapper.INSTANCE.taxResponseFromTax(product.getTax());
        if (promotionResponseFromProduct == null){
            promotionResponseFromProduct = new PromotionResponse();
        } if(taxResponseFromProduct == null){
            taxResponseFromProduct = new TaxResponse();
        }
        ProductResponse productResponse = ProductMapper.INSTANCE.productResponseFromProduct(productRepository.save(product), categoryResponse, promotionResponseFromProduct, taxResponseFromProduct);
        return responseBuilder.buildResponse(HttpStatus.CREATED, "Producto agregado exitosamente.", productResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponse> productResponseList = productList.stream().
                map(product -> {
                    PromotionResponse promotionResponseFromProduct = PromotionMapper.INSTANCE.promotionResponseFromPromotion(product.getPromotion());
                    TaxResponse taxResponseFromProduct = TaxMapper.INSTANCE.taxResponseFromTax(product.getTax());
                    if (promotionResponseFromProduct == null){
                        promotionResponseFromProduct = new PromotionResponse();
                    } if(taxResponseFromProduct == null){
                        taxResponseFromProduct = new TaxResponse();
                    }
                    Category category = product.getCategory();
                    PromotionResponse promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
                    TaxResponse taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
                    CategoryResponse categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
                    return ProductMapper.INSTANCE.productResponseFromProduct(product, categoryResponse, promotionResponseFromProduct, taxResponseFromProduct);
                }).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Productos.", productResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, ProductRequest request) {
        Product productToEdit = productRepository.findById(id).orElseThrow(()-> new RuntimeException("El producto con id " + id + " no existe."));
        productToEdit.setCode(request.getCode());
        if (productRepository.existsByCodeAndIdNot(productToEdit.getCode(), productToEdit.getId()))
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El producto con código \'"+ request.getCode()+"\' ya existe.");
        productToEdit.setQuantity(request.getQuantity());
        productToEdit.setPrice(request.getPrice());
        productToEdit.setStatus(request.getStatus());
        productToEdit.setMinStock(request.getMinStock());
        productToEdit.setMaxStock(request.getMaxStock());
        PromotionResponse promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(productToEdit.getCategory().getPromotion());
        TaxResponse taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(productToEdit.getCategory().getTax());
        CategoryResponse categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(productToEdit.getCategory(), promotionResponse, taxResponse);
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
        PromotionResponse promotionResponseFromProduct = PromotionMapper.INSTANCE.promotionResponseFromPromotion(productToEdit.getPromotion());
        TaxResponse taxResponseFromProduct = TaxMapper.INSTANCE.taxResponseFromTax(productToEdit.getTax());
        if (promotionResponseFromProduct == null){
            promotionResponseFromProduct = new PromotionResponse();
        } if(taxResponseFromProduct == null){
            taxResponseFromProduct = new TaxResponse();
        }
        ProductResponse productResponse = ProductMapper.INSTANCE.productResponseFromProduct(productRepository.save(productToEdit), categoryResponse, promotionResponseFromProduct, taxResponseFromProduct);
        return responseBuilder.buildResponse(HttpStatus.OK, "Producto actualizado exitosamente.", productResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findByID(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("El producto con id " + id + " no existe."));
        PromotionResponse promotionResponseFromProduct = PromotionMapper.INSTANCE.promotionResponseFromPromotion(product.getPromotion());
        TaxResponse taxResponseFromProduct = TaxMapper.INSTANCE.taxResponseFromTax(product.getTax());
        if (promotionResponseFromProduct == null){
            promotionResponseFromProduct = new PromotionResponse();
        } if(taxResponseFromProduct == null){
            taxResponseFromProduct = new TaxResponse();
        }
        Category category = product.getCategory();
        PromotionResponse promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
        TaxResponse taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
        CategoryResponse categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
        ProductResponse productResponse = ProductMapper.INSTANCE.productResponseFromProduct(product, categoryResponse, promotionResponseFromProduct, taxResponseFromProduct);
        return responseBuilder.buildResponse(HttpStatus.OK, "Producto encontrado exitosamente.", productResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> delete(UUID id) {
        if (productRepository.existsById(id)){
            productRepository.deleteById(id);
            return responseBuilder.buildResponse(HttpStatus.OK, "Producto removido exitosamente.");
        }
        throw  new RuntimeException("El producto con el identificador: " + id + " no se encuentra.");
    }
}
