package com.capibaracode.backend.api.models.responses;

import com.capibaracode.backend.domain.entities.Payment;
import com.capibaracode.backend.domain.entities.PurchaseDetail;
import com.capibaracode.backend.domain.entities.Supplier;
import com.capibaracode.backend.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponse {

    private UUID id;

    private String purchaseNumber;

    private LocalDate purchaseDate;

    private BigDecimal subtotalExcludingIVA;

    private Double iva;

    private BigDecimal total;

    private Boolean status;

    private SupplierResponse supplier;

    private PaymentResponse payment;

    private UserResponse user;

}
