package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.requests.CategoryRequest;
import com.capibaracode.backend.api.models.responses.CategoryResponse;
import com.capibaracode.backend.api.models.responses.PromotionResponse;
import com.capibaracode.backend.api.models.responses.TaxResponse;
import com.capibaracode.backend.domain.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);


    @Mapping(target = "promotion", ignore = true)
    @Mapping(target = "tax", ignore = true)
    Category categoryFromCategoryRequest(CategoryRequest request);

    @Mapping(target = "id" , source = "category.id")
    @Mapping(target = "promotion" , source = "promotion")
    @Mapping(target = "tax", source = "tax")
    @Mapping(target = "status", source = "category.status")
    CategoryResponse categoryResponseFromCategory(Category category, PromotionResponse promotion, TaxResponse tax);

}
