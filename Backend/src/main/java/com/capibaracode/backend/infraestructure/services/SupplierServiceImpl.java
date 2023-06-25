package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.SupplierRequest;
import com.capibaracode.backend.api.models.responses.SupplierResponse;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.Supplier;
import com.capibaracode.backend.domain.repositories.SupplierRepository;
import com.capibaracode.backend.infraestructure.abstract_services.ISupplierService;
import com.capibaracode.backend.util.mappers.SupplierMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SupplierServiceImpl implements ISupplierService {

    private final SupplierRepository supplierRepository;
    private final CustomResponseBuilder responseBuilder;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, CustomResponseBuilder responseBuilder) {
        this.supplierRepository = supplierRepository;
        this.responseBuilder = responseBuilder;
    }


    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(SupplierRequest request) {
        if (supplierRepository.existsByIdentification(request.getIdentification())){
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El proveedor con identificación \'"+ request.getIdentification() +"\' ya existe.");
        }
        Supplier supplier = SupplierMapper.INSTANCE.supplierFromSupplierRequest(request);
        SupplierResponse supplierResponse = SupplierMapper.INSTANCE.supplierResponseFromSupplier(supplierRepository.save(supplier));
        return responseBuilder.buildResponse(HttpStatus.CREATED, "Proveedor agregado exitosamente.", supplierResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        List<Supplier> supplierList = supplierRepository.findAll();
        List<SupplierResponse> supplierResponseList = supplierList.stream().map(SupplierMapper.INSTANCE::supplierResponseFromSupplier).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Proveedores", supplierResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, SupplierRequest request) {
        Supplier supplierToEdit = supplierRepository.findById(id).orElseThrow(()-> new RuntimeException("El proveedor con id "+ id +" no existe."));
        supplierToEdit.setName(request.getName());
        supplierToEdit.setEmail(request.getEmail());
        supplierToEdit.setAddress(request.getAddress());
        supplierToEdit.setTelephone(request.getTelephone());
        supplierToEdit.setStatus(request.getStatus());
        SupplierResponse supplierResponse = SupplierMapper.INSTANCE.supplierResponseFromSupplier(supplierRepository.save(supplierToEdit));
        return responseBuilder.buildResponse(HttpStatus.OK, "Proveedor actualizado exitosamente.", supplierResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getByID(UUID id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(()-> new RuntimeException("El proveedor con id "+ id +" no existe."));
        SupplierResponse supplierResponse = SupplierMapper.INSTANCE.supplierResponseFromSupplier(supplier);
        return responseBuilder.buildResponse(HttpStatus.OK, "Proveedor solicitado, encontrado exitosamente.", supplierResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> delete(UUID id) {
        if (supplierRepository.existsById(id)){
            supplierRepository.deleteById(id);
            return responseBuilder.buildResponse(HttpStatus.OK, "Proveedor removido exitosamente.");
        }
        throw new RuntimeException("El proveedor con el identificador: " + id + " no se encuentra.");
    }

}
