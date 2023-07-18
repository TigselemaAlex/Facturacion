package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Company;
import com.capibaracode.backend.domain.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    List<Invoice> findAllByUserCompany(Company company);
}
