package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.PromotionRequest;
import com.capibaracode.backend.api.models.responses.PromotionResponse;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.Promotion;
import com.capibaracode.backend.domain.repositories.PromotionRepository;
import com.capibaracode.backend.infraestructure.abstract_services.IPromotionService;
import com.capibaracode.backend.util.mappers.PromotionMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PromotionServiceImpl implements IPromotionService {

    private final PromotionRepository promotionRepository;
    private final CustomResponseBuilder responseBuilder;

    @Autowired
    public PromotionServiceImpl(PromotionRepository promotionRepository, CustomResponseBuilder responseBuilder) {
        this.promotionRepository = promotionRepository;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(PromotionRequest request) {
        Promotion promotion = PromotionMapper.INSTANCE.promotionFromPromotionRequest(request);
        PromotionResponse promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(promotionRepository.save(promotion));
        return responseBuilder.buildResponse(HttpStatus.CREATED, "Promoción agregada exitosamente.", promotionResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        List<Promotion> promotionList = promotionRepository.findAll();
        List<PromotionResponse> promotionResponseList = promotionList.stream().map(PromotionMapper.INSTANCE::promotionResponseFromPromotion).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Promociones", promotionResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, PromotionRequest request) {
        Promotion promotionToEdit = promotionRepository.findById(id).orElseThrow(()-> new RuntimeException("La promoción con id " + id + " no existe."));
        promotionToEdit.setDescription(request.getDescription());
        promotionToEdit.setValue(request.getValue());
        promotionToEdit.setStatus(request.getStatus());
        PromotionResponse promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(promotionRepository.save(promotionToEdit));
        return responseBuilder.buildResponse(HttpStatus.OK, "Promoción actualizada exitosamente.", promotionResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findByID(UUID id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(()-> new RuntimeException("La promoción con id " + id + " no existe."));
        PromotionResponse promotionResponse = PromotionMapper.INSTANCE.promotionResponseFromPromotion(promotion);
        return responseBuilder.buildResponse(HttpStatus.OK, "Promoción encontrada.", promotionResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> delete(UUID id) {
        if (promotionRepository.existsById(id)) {
            promotionRepository.deleteById(id);
            return responseBuilder.buildResponse(HttpStatus.OK, "Promoción removido exitosamente.");
        }
        throw  new RuntimeException("La promoción con el identificador: " + id + " no se encuentra.");
    }
}
