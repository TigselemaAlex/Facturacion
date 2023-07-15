package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    List<Client> findAllByStatusTrue();

    Optional<Client> findByIdentification(String identification);

    @Query("SELECT c.identification FROM Client c")
    List<String> findAllIdentification();
}
