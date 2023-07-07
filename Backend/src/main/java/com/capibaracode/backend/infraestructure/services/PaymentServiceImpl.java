package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.PaymentRequest;
import com.capibaracode.backend.api.models.responses.PaymentResponse;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.Payment;
import com.capibaracode.backend.domain.repositories.PaymentRepository;
import com.capibaracode.backend.infraestructure.abstract_services.IPaymentService;
import com.capibaracode.backend.util.mappers.PaymentMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository paymentRepository;

    private final CustomResponseBuilder responseBuilder;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, CustomResponseBuilder responseBuilder) {
        this.paymentRepository = paymentRepository;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(PaymentRequest request) {
        if (paymentRepository.existsByPayment(request.getPayment()))return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El tipo de pago con nombre \'"+ request.getPayment()+"\' ya existe.");
        Payment payment = PaymentMapper.INSTANCE.paymentFromPaymentRequest(request);
        PaymentResponse paymentResponse = PaymentMapper.INSTANCE.paymentResponseFromPayment(paymentRepository.save(payment));
        return responseBuilder.buildResponse(HttpStatus.CREATED, "Tipo de pago agregado exitosamente.", paymentResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        List<Payment> paymentList = paymentRepository.findAll();
        List<PaymentResponse> paymentResponseList = paymentList.stream().map(PaymentMapper.INSTANCE::paymentResponseFromPayment).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Tipos de pago.", paymentResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, PaymentRequest request) {
        Payment paymentToEdit = paymentRepository.findById(id).orElseThrow(()->new RuntimeException("El tipo de pago con id "+id+" no existe."));
        boolean categoryExists = paymentRepository.existsByPaymentAndIdNot(request.getPayment(), paymentToEdit.getId());
        if (categoryExists){
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El tipo de pago con nombre \'"+ request.getPayment()+"\' ya existe.");
        }
        paymentToEdit.setPayment(request.getPayment());
        paymentToEdit.setStatus(request.getStatus());
        PaymentResponse paymentResponse = PaymentMapper.INSTANCE.paymentResponseFromPayment(paymentRepository.save(paymentToEdit));
        return responseBuilder.buildResponse(HttpStatus.OK, "Tipo de pago actualizado exitosamente.", paymentResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getByID(UUID id) {
        if (paymentRepository.existsById(id)){
            Payment payment = paymentRepository.findById(id).orElseThrow(()->new RuntimeException("El tipo de pago con id "+id+" no existe."));
            PaymentResponse paymentResponse = PaymentMapper.INSTANCE.paymentResponseFromPayment(payment);
            return responseBuilder.buildResponse(HttpStatus.OK, "Tipo de pago encontrado exitosamente.", paymentResponse);
        }
        return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "El tipo de pago con id "+id+" no se encuentra.");
    }

}
