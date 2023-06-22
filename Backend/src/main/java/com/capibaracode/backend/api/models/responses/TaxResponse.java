package com.capibaracode.backend.api.models.responses;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaxResponse {

    private UUID id;

    private String tax;

    private Double percentage;

    private Boolean status;

}
