package com.capibaracode.backend.util.mappers;


import com.capibaracode.backend.api.models.responses.InvoiceDetailsResponse;
import com.capibaracode.backend.api.models.responses.ProductResponse;
import com.capibaracode.backend.domain.entities.InvoiceDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvoiceDetailMapper {

    InvoiceDetailMapper INSTANCE = Mappers.getMapper(InvoiceDetailMapper.class);

    @Mapping(target = "product", source = "productResponse")
    @Mapping(target = "id", source = "invoiceDetail.id")
    @Mapping(target = "price", source = "invoiceDetail.price")
    @Mapping(target = "quantity", source = "invoiceDetail.quantity")
    InvoiceDetailsResponse invoiceDetailsResponseFromInvoiceDetail(InvoiceDetail invoiceDetail, ProductResponse productResponse);
}
