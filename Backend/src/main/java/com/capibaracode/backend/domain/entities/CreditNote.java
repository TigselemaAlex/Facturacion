package com.capibaracode.backend.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class CreditNote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String creditNoteNumber;
    private LocalDate issueDate;
    @ManyToOne
    private Invoice invoice;
    @OneToMany
    private List<CreditNoteDetail> details;
}
