package com.capibaracode.backend.infraestructure.services;


import com.capibaracode.backend.api.models.requests.TaxRequest;
import com.capibaracode.backend.api.models.responses.TaxResponse;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.Tax;
import com.capibaracode.backend.domain.repositories.TaxRepository;
import com.capibaracode.backend.infraestructure.abstract_services.ITaxService;
import com.capibaracode.backend.util.mappers.TaxMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TaxServiceImpl implements ITaxService {

    private final TaxRepository taxRepository;
    private final CustomResponseBuilder responseBuilder;

    @Autowired
    public TaxServiceImpl(TaxRepository taxRepository, CustomResponseBuilder responseBuilder) {
        this.taxRepository = taxRepository;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(TaxRequest request) {
        Tax tax = TaxMapper.INSTANCE.taxFromTaxRequest(request);
        TaxResponse taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(taxRepository.save(tax));
        return responseBuilder.buildResponse(HttpStatus.CREATED, "Impuesto agregado exitosamente", taxResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        List<Tax> taxList = taxRepository.findAll();
        List<TaxResponse> taxResponseList = taxList.stream().map(TaxMapper.INSTANCE::taxResponseFromTax).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Impuestos", taxResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, TaxRequest request) {
        Tax taxToEdit = taxRepository.findById(id).orElseThrow(()->new RuntimeException("El impuesto con id " + id + " no existe."));
        taxToEdit.setTax(request.getTax());
        taxToEdit.setPercentage(request.getPercentage());
        taxToEdit.setStatus(request.getStatus());
        TaxResponse taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(taxToEdit);
        return responseBuilder.buildResponse(HttpStatus.OK, "Impuesto actualizado exitosamente.", taxResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findByNameTax(String taxName) {
        Tax tax = taxRepository.findByTax(taxName).orElseThrow(()-> new RuntimeException(taxName + " no existe."));
        TaxResponse taxResponse = TaxMapper.INSTANCE.taxResponseFromTax(tax);
        return responseBuilder.buildResponse(HttpStatus.OK,"Impuesto encontrado exitosamente.", taxResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> delete(UUID id) {
        if (taxRepository.existsById(id)) {
            taxRepository.deleteById(id);
            return responseBuilder.buildResponse(HttpStatus.OK, "Impuesto removido exitosamente");
        }
        throw  new RuntimeException("El impuesto con el identificador: " + id + " no se encuentra.");
    }
}
