package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    boolean existsByPayment(String paymentName);

    boolean existsByPaymentAndIdNot(String newPaymentName, UUID id);

}
