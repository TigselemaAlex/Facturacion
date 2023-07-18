package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.responses.ClientResponse;
import com.capibaracode.backend.api.models.responses.InvoiceDetailsResponse;
import com.capibaracode.backend.api.models.responses.InvoiceResponse;
import com.capibaracode.backend.api.models.responses.UserResponseDTO;
import com.capibaracode.backend.domain.entities.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Mapping(target = "details", source = "detailsResponses")
    @Mapping(target = "client", source = "clientResponse")
    @Mapping(target = "user", source = "userResponseDTO")
    @Mapping(target = "id", source = "invoice.id")
    @Mapping(target = "status", source = "invoice.status")
    InvoiceResponse invoiceResponseFromInvoice(Invoice invoice, List<InvoiceDetailsResponse> detailsResponses, UserResponseDTO userResponseDTO, ClientResponse clientResponse);
}
