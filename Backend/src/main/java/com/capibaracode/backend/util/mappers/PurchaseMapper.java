package com.capibaracode.backend.util.mappers;


import com.capibaracode.backend.api.models.requests.PurchaseRequest;
import com.capibaracode.backend.api.models.responses.PaymentResponse;
import com.capibaracode.backend.api.models.responses.PurchaseResponse;
import com.capibaracode.backend.api.models.responses.SupplierResponse;
import com.capibaracode.backend.api.models.responses.UserResponse;
import com.capibaracode.backend.domain.entities.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "user", ignore = true)
    Purchase purchaseFromPurchaseRequest(PurchaseRequest request);

    @Mapping(target = "id", source = "purchase.id")
    @Mapping(target = "purchaseNumber", source = "purchase.purchaseNumber")
    @Mapping(target = "purchaseDate", source = "purchase.purchaseDate")
    @Mapping(target = "subtotalExcludingIVA", source = "purchase.subtotalExcludingIVA")
    @Mapping(target = "iva", source = "purchase.iva")
    @Mapping(target = "total", source = "purchase.total")
    @Mapping(target = "status", source = "purchase.status")
    @Mapping(target = "supplier", source = "supplier")
    @Mapping(target = "payment", source = "payment")
    @Mapping(target = "user", source = "user")
    PurchaseResponse purchaseResponseFromPurchase(Purchase purchase, SupplierResponse supplier, PaymentResponse payment, UserResponse user);

}
