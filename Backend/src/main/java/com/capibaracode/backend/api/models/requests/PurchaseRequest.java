package com.capibaracode.backend.api.models.requests;

import com.capibaracode.backend.domain.entities.Payment;
import com.capibaracode.backend.domain.entities.PurchaseDetail;
import com.capibaracode.backend.domain.entities.Supplier;
import com.capibaracode.backend.domain.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseRequest {

    @NotBlank(message = "El numero de compra es obligatorio.")
    private String purchaseNumber;

    @NotBlank(message = "La fecha es obligatorio.")
    private LocalDate purchaseDate;

    @NotNull(message = "El Subtotal sin IVA es obligatorio.")
    private BigDecimal subtotalExcludingIVA;

    @NotNull(message = "El IVA es obligatorio.")
    private Double iva;

    @NotNull(message = "El Total es obligatorio.")
    private BigDecimal total;

    @NotNull(message = "El Total es obligatorio.")
    private Boolean status;

    @NotNull(message = "El nombre de proveedor es obligatorio.")
    private UUID supplier;

    @NotNull(message = "El tipo de pago es obligatorio.")
    private UUID payment;

    @NotNull(message = "El usuario que genera la compra es obligatorio.")
    private UUID user;

    @NotNull(message = "La lista detallada de compra es obligatorio.")
    private List<PurchaseDetail> details;

}
