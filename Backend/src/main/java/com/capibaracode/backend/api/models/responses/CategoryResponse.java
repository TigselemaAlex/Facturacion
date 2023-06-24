package com.capibaracode.backend.api.models.responses;

import com.capibaracode.backend.domain.entities.Promotion;
import com.capibaracode.backend.domain.entities.Tax;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryResponse {

    private UUID id;

    private String category;

    private Boolean status;

    private PromotionResponse promotion;

    private TaxResponse tax;
}
