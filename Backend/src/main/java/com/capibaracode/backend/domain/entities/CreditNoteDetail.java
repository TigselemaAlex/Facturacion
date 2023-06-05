package com.capibaracode.backend.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class CreditNoteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private BigDecimal price;
    private Integer quantity;
    private Double discount;
    private Double subtotal;
    @ManyToOne
    private CreditNote creditNote;
}
