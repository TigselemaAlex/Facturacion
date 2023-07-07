package com.capibaracode.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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

    //@JsonManagedReference
    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private List<PurchaseDetail> details = new ArrayList<>();

    public void setNewList(List<PurchaseDetail> newDetails){
        this.details = newDetails;
    }

    public void addPurchaseDetail(PurchaseDetail purchaseDetail) {
        if (purchaseDetail != null) {
            if (details == null) { details = new ArrayList<>(); }
            details.add(purchaseDetail);
        }
    }

    @PrePersist
    public void prePersist(){
        this.purchaseDate = LocalDate.now();
    }

}
