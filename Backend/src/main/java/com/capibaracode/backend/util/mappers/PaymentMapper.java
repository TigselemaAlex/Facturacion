package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.requests.PaymentRequest;
import com.capibaracode.backend.api.models.responses.PaymentResponse;
import com.capibaracode.backend.domain.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    Payment paymentFromPaymentRequest(PaymentRequest request);

    PaymentResponse paymentResponseFromPayment(Payment payment);

}
