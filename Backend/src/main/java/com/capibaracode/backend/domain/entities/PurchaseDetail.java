package com.capibaracode.backend.domain.entities;

import jakarta.persistence.*;
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
    private Integer quantity;
    private String code;
    private Double discount;
    private BigDecimal subtotal;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Purchase purchase;
}
