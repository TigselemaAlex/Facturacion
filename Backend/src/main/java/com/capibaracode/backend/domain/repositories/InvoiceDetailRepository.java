package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, UUID> {
}
