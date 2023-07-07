package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.PurchaseDetailRequest;
import com.capibaracode.backend.api.models.responses.*;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.Category;
import com.capibaracode.backend.domain.entities.Product;
import com.capibaracode.backend.domain.entities.Purchase;
import com.capibaracode.backend.domain.entities.PurchaseDetail;
import com.capibaracode.backend.domain.repositories.CategoryRepository;
import com.capibaracode.backend.domain.repositories.ProductRepository;
import com.capibaracode.backend.domain.repositories.PurchaseDetailRepository;
import com.capibaracode.backend.domain.repositories.PurchaseRepository;
import com.capibaracode.backend.infraestructure.abstract_services.IPurchaseDetailService;
import com.capibaracode.backend.util.mappers.*;
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
public class PurchaseDetailServiceImpl implements IPurchaseDetailService {

    private final PurchaseDetailRepository detailRepository;

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final PurchaseRepository purchaseRepository;

    private final CustomResponseBuilder responseBuilder;

    @Autowired
    public PurchaseDetailServiceImpl(PurchaseDetailRepository detailRepository, ProductRepository productRepository, CategoryRepository categoryRepository, PurchaseRepository purchaseRepository, CustomResponseBuilder responseBuilder) {
        this.detailRepository = detailRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.purchaseRepository = purchaseRepository;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(PurchaseDetailRequest request) {
        PurchaseDetail purchaseDetail = PurchaseDetailMapper.INSTANCE.purchaseDetailFromPurchaseDetailRequest(request);
        ProductResponse productResponse = null;
        Product product;
        if (productRepository.existsById(request.getProduct())){
            product = productRepository.findById(request.getProduct()).orElseThrow(()-> new RuntimeException("El producto con id "+ request.getProduct()+ " no existe."));
            purchaseDetail.setProduct(product);//chevere
            CategoryResponse categoryResponse = null;
            if (product.getCategory() != null){
                Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(()-> new RuntimeException("La categoria con id "+ product.getCategory().getId()+ " no existe."));
                PromotionResponse promotionResponse = null;
                TaxResponse taxResponse = null;
                if (category.getPromotion() != null){
                    promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
                }
                if (category.getTax() != null){
                    taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
                }
                categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
                productResponse = ProductMapper.INSTANCE.productResponseFromProduct(product, categoryResponse, promotionResponse, taxResponse);
            }
        }else{
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El producto con id "+ request.getProduct()+ " no existe.");
        }
        if (purchaseRepository.existsById(request.getPurchase())){
            Purchase purchase = purchaseRepository.findById(request.getPurchase()).orElseThrow(()-> new RuntimeException("La compra con id "+ request.getPurchase()+ " no existe."));
            purchaseDetail.setPurchase(purchase);
            if (detailRepository.existsByProductAndPurchase(product, purchase))
                return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El producto con nombre \'"+ product.getName()+"\' no puede estar mas de una vez en el mismo detalle compra.");
        }else{
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "La compra con id "+ request.getPurchase()+ " no existe.");
        }
        PurchaseDetailResponse detailResponse = PurchaseDetailMapper.INSTANCE.purchaseDetailResponseFromPurchaseDetail(detailRepository.save(purchaseDetail), productResponse);
        return responseBuilder.buildResponse(HttpStatus.CREATED, "Detalle de compra agregado exitosamente.", detailResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        List<PurchaseDetail> purchaseDetailList = detailRepository.findAll();
        List<PurchaseDetailResponse> purchaseDetailResponseList = purchaseDetailList.stream().
                map(purchaseDetail -> {
                    ProductResponse productResponse = null;
                    CategoryResponse categoryResponse = null;
                    if (purchaseDetail.getProduct().getCategory() != null){
                        Category category = categoryRepository.findById(purchaseDetail.getProduct().getCategory().getId()).orElseThrow(()-> new RuntimeException("La categoria con id "+ purchaseDetail.getProduct().getCategory().getId()+ " no existe."));
                        PromotionResponse promotionResponse = null;
                        TaxResponse taxResponse = null;
                        if (category.getPromotion() != null){
                            promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
                        }
                        if (category.getTax() != null){
                            taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
                        }
                        categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
                        productResponse = ProductMapper.INSTANCE.productResponseFromProduct(purchaseDetail.getProduct(), categoryResponse, promotionResponse, taxResponse);
                    }
                    return PurchaseDetailMapper.INSTANCE.purchaseDetailResponseFromPurchaseDetail(purchaseDetail, productResponse);
                }).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Detalle de Compra.", purchaseDetailResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, PurchaseDetailRequest request) {
        PurchaseDetail purchaseDetailToEdit = detailRepository.findById(id).orElseThrow(()-> new RuntimeException("El detalle de compra con id " + id + " no existe."));
        purchaseDetailToEdit.setQuantity(request.getQuantity());
        purchaseDetailToEdit.setSubtotal(request.getSubtotal());
        purchaseDetailToEdit.setStatus(request.getStatus());
        ProductResponse productResponse = null;
        CategoryResponse categoryResponse = null;
        if (purchaseDetailToEdit.getProduct().getCategory() != null){
            Category category = categoryRepository.findById(purchaseDetailToEdit.getProduct().getCategory().getId()).orElseThrow(()-> new RuntimeException("La categoria con id "+ purchaseDetailToEdit.getProduct().getCategory().getId()+ " no existe."));
            PromotionResponse promotionResponse = null;
            TaxResponse taxResponse = null;
            if (category.getPromotion() != null){
                promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
            }
            if (category.getTax() != null){
                taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
            }
            categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
            productResponse = ProductMapper.INSTANCE.productResponseFromProduct(purchaseDetailToEdit.getProduct(), categoryResponse, promotionResponse, taxResponse);
        }
        PurchaseDetailResponse purchaseDetailResponse = PurchaseDetailMapper.INSTANCE.purchaseDetailResponseFromPurchaseDetail(purchaseDetailToEdit, productResponse);
        return responseBuilder.buildResponse(HttpStatus.OK, "Detalle de compra actualizado exitosamente.", purchaseDetailResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getByIdPurchase(UUID id) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow(()-> new RuntimeException("La compra con id en el detalle de compra: " + id + " no existe."));
        List<PurchaseDetail> purchaseDetailList = detailRepository.findByPurchase(purchase);
        List<PurchaseDetailResponse> purchaseDetailResponseList = purchaseDetailList.stream().
                map(purchaseDetail -> {
                    ProductResponse productResponse = null;
                    CategoryResponse categoryResponse = null;
                    if (purchaseDetail.getProduct().getCategory() != null){
                        Category category = categoryRepository.findById(purchaseDetail.getProduct().getCategory().getId()).orElseThrow(()-> new RuntimeException("La categoria con id "+ purchaseDetail.getProduct().getCategory().getId()+ " no existe."));
                        PromotionResponse promotionResponse = null;
                        TaxResponse taxResponse = null;
                        if (category.getPromotion() != null){
                            promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(category.getPromotion());
                        }
                        if (category.getTax() != null){
                            taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(category.getTax());
                        }
                        categoryResponse = CategoryMapper.INSTANCE.categoryResponseFromCategory(category, promotionResponse, taxResponse);
                        productResponse = ProductMapper.INSTANCE.productResponseFromProduct(purchaseDetail.getProduct(), categoryResponse, promotionResponse, taxResponse);
                    }
                    PurchaseDetailResponse purchaseDetailResponse = PurchaseDetailMapper.INSTANCE.purchaseDetailResponseFromPurchaseDetail(purchaseDetail, productResponse);
                    purchaseDetailResponse.setPurchase(purchaseDetail.getPurchase().getId());
                    return purchaseDetailResponse;
                }).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Detalle de Compra por Compra.", purchaseDetailResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> changeStatus(UUID id) {
        if (detailRepository.existsById(id)){
            PurchaseDetail purchaseDetail = detailRepository.findById(id).orElseThrow(()-> new RuntimeException("El detalle de compra con id " + id + " no existe."));
            boolean statusValue;
            if (purchaseDetail.getStatus()){
                purchaseDetail.setStatus(false);
            }else{
                purchaseDetail.setStatus(true);
            }
            statusValue = purchaseDetail.getStatus();
            return responseBuilder.buildResponse(HttpStatus.OK, "Cambio de estado exitosamente.", statusValue);
        }
        throw new RuntimeException("El detalle de compra con el identificador: " + id + " no se encuentra.");
    }
}
