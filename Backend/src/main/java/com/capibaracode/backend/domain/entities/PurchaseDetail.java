package com.capibaracode.backend.domain.entities;

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
    @Min(value = 0, message = "")
    private Integer quantity;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Double discount;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Purchase purchase;
}
