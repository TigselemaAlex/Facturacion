package com.capibaracode.backend.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class InvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Double discount;
    private Integer quantity;
    private Double price;
    private BigDecimal subtotal;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Invoice invoice;
}
