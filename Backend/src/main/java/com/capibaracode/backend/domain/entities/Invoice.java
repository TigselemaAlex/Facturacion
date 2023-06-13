package com.capibaracode.backend.domain.entities;

import com.capibaracode.backend.util.enums.InvoiceStatus;
import jakarta.persistence.*;
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
@Entity
public class Invoice {
    @Id
    private UUID id;
    private String invoiceNumber;
    private InvoiceStatus status;
    private LocalDate issueDate;
    private String description;
    private Double iva;
    private BigDecimal subtotalExcludingIVA;
    private BigDecimal total;

    @ManyToOne
    private User user;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Payment payment;
    @OneToMany(mappedBy = "invoice")
    private List<InvoiceDetail> details;
}
