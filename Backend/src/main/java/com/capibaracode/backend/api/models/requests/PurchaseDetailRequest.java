package com.capibaracode.backend.api.models.requests;

import com.capibaracode.backend.domain.entities.Product;
import com.capibaracode.backend.domain.entities.Purchase;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseDetailRequest {

    @NotNull(message = "La cantidad es obligatorio.")
    @Min(value = 0, message = "La cantidad no puede ser menor a 0.")
    private Integer quantity;

    @NotNull(message = "El descuento es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento no puede ser menor a 0.")
    private Double discount;

    @NotNull(message = "El subtotal es oblligatorio.")
    @Min(value = 0, message = "El subtotal no puede ser menor a 0.")
    private BigDecimal subtotal;

    @NotBlank(message = "El id del producto es obligatorio.")
    private UUID product;

    @NotBlank(message = "El id de la compra es obligatorio.")
    private UUID purchase;

}
