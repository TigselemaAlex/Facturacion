package com.capibaracode.backend.api.models.requests;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class TaxRequest {

    @NotBlank(message = "El nombre del impuesto es obligatorio")
    @Size(max = 50, message = "El nombre de la categoría no puede tener más de 50 caracteres")
    private String tax;

    @NotNull(message = "El porcetaje del impuesto es obligatorio: Eje. 0.0")
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    private Double percentage;

    @NotNull(message = "El estado del impuesto es obligatorio")
    private Boolean status;

}
