package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.requests.CategoryRequest;
import com.capibaracode.backend.api.models.responses.CategoryResponse;
import com.capibaracode.backend.domain.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);


    Category categoryFromCategoryRequest(CategoryRequest request);

    CategoryResponse categoryResponseFromCategory(Category category);

}
