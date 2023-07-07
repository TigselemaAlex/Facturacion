package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.requests.ProductRequest;
import com.capibaracode.backend.api.models.requests.PurchaseDetailRequest;
import com.capibaracode.backend.api.models.responses.ProductResponse;
import com.capibaracode.backend.api.models.responses.PurchaseDetailResponse;
import com.capibaracode.backend.domain.entities.PurchaseDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PurchaseDetailMapper {

    PurchaseDetailMapper INSTANCE = Mappers.getMapper(PurchaseDetailMapper.class);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "purchase", ignore = true)
    PurchaseDetail purchaseDetailFromPurchaseDetailRequest(PurchaseDetailRequest request);

    @Mapping(target = "id", source = "purchaseDetail.id")
    @Mapping(target = "quantity", source = "purchaseDetail.quantity")
    @Mapping(target = "status", source = "purchaseDetail.status")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "purchase", ignore = true)
    PurchaseDetailResponse purchaseDetailResponseFromPurchaseDetail(PurchaseDetail purchaseDetail, ProductResponse product);

}
