package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.requests.ProductRequest;
import com.capibaracode.backend.api.models.responses.CategoryResponse;
import com.capibaracode.backend.api.models.responses.ProductResponse;
import com.capibaracode.backend.api.models.responses.PromotionResponse;
import com.capibaracode.backend.api.models.responses.TaxResponse;
import com.capibaracode.backend.domain.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "promotion", ignore = true)
    @Mapping(target = "tax", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    Product productFromProductRequest(ProductRequest request);

    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "code", source = "product.code")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "quantity", source = "product.quantity")
    @Mapping(target = "status", source = "product.status")
    @Mapping(target = "minStock", source = "product.minStock")
    @Mapping(target = "maxStock", source = "product.maxStock")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "promotion", source = "promotion")
    @Mapping(target = "tax", source = "tax")
    @Mapping(target = "supplier", ignore = true)
    ProductResponse productResponseFromProduct(Product product, CategoryResponse category, PromotionResponse promotion, TaxResponse tax);

    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "code", source = "product.code")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "quantity", source = "product.quantity")
    @Mapping(target = "status", source = "product.status")
    @Mapping(target = "minStock", source = "product.minStock")
    @Mapping(target = "maxStock", source = "product.maxStock")
    @Mapping(target = "supplier", ignore = true)
    ProductResponse productResponseFromProductWithoutRelations(Product product);


}
