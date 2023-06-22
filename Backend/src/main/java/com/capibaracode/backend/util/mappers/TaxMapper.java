package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.requests.TaxRequest;
import com.capibaracode.backend.api.models.responses.TaxResponse;
import com.capibaracode.backend.domain.entities.Tax;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaxMapper {

    TaxMapper INSTANCE = Mappers.getMapper(TaxMapper.class);

    Tax taxFromTaxRequest(TaxRequest request);

    TaxResponse taxResponseFromTax(Tax tax);

}
