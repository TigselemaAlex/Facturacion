package com.capibaracode.backend.api.models.responses;

import com.capibaracode.backend.domain.entities.Promotion;
import com.capibaracode.backend.domain.entities.Tax;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CategoryResponse {

    private String category;

    private Boolean status;

    private Promotion promotion;

    private Tax tax;
}
