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

    @Column(nullable = false, length = 30)
    private String purchaseNumber;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Column(nullable = false)
    private BigDecimal subtotalExcludingIVA;

    @Column(nullable = false)
    private Double iva;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne
    private Supplier supplier;
    @ManyToOne
    private Payment payment;
    @ManyToOne
    private User user;
    @OneToMany (mappedBy = "purchase")
    private List<PurchaseDetail> details;
}
