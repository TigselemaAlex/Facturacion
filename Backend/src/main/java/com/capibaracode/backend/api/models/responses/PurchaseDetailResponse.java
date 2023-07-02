package com.capibaracode.backend.api.models.responses;


import com.capibaracode.backend.domain.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDetailResponse {

    private UUID id;

    private Integer quantity;

    private Double discount;

    private BigDecimal subtotal;

    private ProductResponse product;

    private UUID purchase;

}
