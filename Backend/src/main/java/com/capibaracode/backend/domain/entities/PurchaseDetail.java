package com.capibaracode.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Min(value = 0, message = "El valor no puede ser menor a 0.")
    private Integer quantity;

    @Column(nullable = false)
    @Min(value = 0, message = "El valor no puede ser menor a 0.")
    private Double discount;

    @Column(nullable = false)
    @Min(value = 0, message = "El valor no puede ser menor a 0.")
    private BigDecimal subtotal;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne
    private Product product;

    //@JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
}
