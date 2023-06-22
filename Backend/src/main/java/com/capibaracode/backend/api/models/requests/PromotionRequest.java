package com.capibaracode.backend.api.models.requests;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class PromotionRequest {

    @NotBlank(message = "La descripción de la promoción es obligatorio.")
    @Size(max = 100, message = "La descripción no puede tener más de 100 caracteres.")
    private String description;

    @NotNull(message = "El porcetaje de descuento es obligatorio: Eje. 0.0.")
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    private Double value;

    @NotNull(message = "El estado de la promoción es obligatorio.")
    private Boolean status;

}
