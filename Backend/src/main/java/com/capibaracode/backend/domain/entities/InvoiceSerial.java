package com.capibaracode.backend.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceSerial {
    @Id
    private Long id;
    private String serial;
    private String sequential;
}
