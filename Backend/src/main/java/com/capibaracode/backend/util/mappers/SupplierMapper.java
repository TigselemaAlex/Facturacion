package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.requests.SupplierRequest;
import com.capibaracode.backend.api.models.responses.SupplierResponse;
import com.capibaracode.backend.domain.entities.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    SupplierMapper INSTANCE = Mappers.getMapper(SupplierMapper.class);

    Supplier supplierFromSupplierRequest(SupplierRequest request);

    SupplierResponse supplierResponseFromSupplier(Supplier supplier);

}
