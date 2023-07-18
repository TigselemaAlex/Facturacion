package com.capibaracode.backend.api.models.responses;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private UUID id;

    private String code;

    private String name;

    private Double price;

    private Double quantity;

    private Boolean status;

    private Integer minStock;

    private Integer maxStock;

    private CategoryResponse category;

    private PromotionResponse promotion;

    private TaxResponse tax;

    private UUID supplier;

}
