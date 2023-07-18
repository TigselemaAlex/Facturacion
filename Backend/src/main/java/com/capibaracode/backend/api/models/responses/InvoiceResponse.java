package com.capibaracode.backend.api.models.responses;

import com.capibaracode.backend.util.enums.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceResponse {

    private UUID id;
    private String invoiceNumber;
    private String keyAccess;
    private InvoiceStatus status;
    private LocalDate issueDate;
    private String description;
    private Double iva;
    private BigDecimal subtotalExcludingIVA;
    private BigDecimal total;
    private BigDecimal discount;
    private UserResponseDTO user;
    private ClientResponse client;
    private List<InvoiceDetailsResponse> details = new ArrayList<>();
}
