package com.capibaracode.backend.api.models.requests;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceRequest {

    private UUID client;
    private UUID user;
    private UUID payment;
    private List<InvoiceDetailsRequest> details;
    private Double iva;
    private BigDecimal subtotalExcludingIVA;
    private BigDecimal total;
    private BigDecimal discount;
    private String description;

}
