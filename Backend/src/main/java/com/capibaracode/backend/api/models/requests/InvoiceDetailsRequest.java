package com.capibaracode.backend.api.models.requests;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceDetailsRequest {

    private UUID product;
    private Integer quantity;
    private Double price;
    private Double discount;
    private BigDecimal subtotal;
    private BigDecimal subtotalExcludingIVA;
}
