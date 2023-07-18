package com.capibaracode.backend.api.models.responses;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceDetailsResponse {

    private UUID id;
    private Double discount;
    private Integer quantity;
    private Double price;
    private BigDecimal subtotal;
    private ProductResponse product;
}
