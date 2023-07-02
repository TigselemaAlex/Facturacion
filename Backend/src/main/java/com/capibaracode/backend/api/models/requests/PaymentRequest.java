package com.capibaracode.backend.api.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentRequest {

    @NotBlank(message = "El nombre para el tipo de pago es obligatorio.")
    @Size(max = 100, message = "El nombre para el tipo de pago no puede tener mas de 100 caracteres.")
    private String payment;

    @NotNull(message = "El estado es obligatorio.")
    private Boolean status;

}
