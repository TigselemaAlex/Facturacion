package com.capibaracode.backend.api.models.requests;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductRequest {

    @NotBlank(message = "El código es obligatorio.")
    @Size(max = 10, message = "El código no puede tener más de 10 caracteres.")
    private String code;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres.")
    private String name;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = true, message = "No se admite numeros negativos.")
    private Double price;

    @NotNull(message = "La cantidad es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = true, message = "No se admite numeros negativos.")
    private Double quantity;

    @NotNull(message = "El estado es obligatorio.")
    private Boolean status;

    @NotNull(message = "El mínimo de stock es obligatorio.")
    @Min(value = 0, message = "El mínimo de stock debe ser mayor o igual a cero.")
    private Integer minStock;

    @NotNull(message = "El máximo de stock es obligatorio.")
    private Integer maxStock;

    private UUID category;

    private UUID promotion;

    private UUID tax;

    @NotNull(message = "El proveedor es obligatorio.")
    private UUID supplier;

}
