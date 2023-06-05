package com.capibaracode.backend.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String purchaseNumber;
    private LocalDate purchaseDate;
    private BigDecimal subtotalExcludingIVA;
    private Double iva;
    private BigDecimal total;
    @ManyToOne
    private Supplier supplier;
    @ManyToOne
    private Payment payment;
    @ManyToOne
    private User user;
    @OneToMany
    private List<PurchaseDetail> details;
}
