package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.requests.PromotionRequest;
import com.capibaracode.backend.api.models.responses.PromotionResponse;
import com.capibaracode.backend.domain.entities.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);

    Promotion promotionFromPromotionRequest(PromotionRequest request);

    PromotionResponse promotionResponseFromPromotion(Promotion promotion);

}
