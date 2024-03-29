package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.ClientRequest;
import com.capibaracode.backend.api.models.responses.ClientResponse;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.Client;
import com.capibaracode.backend.domain.repositories.ClientRepository;
import com.capibaracode.backend.infraestructure.abstract_services.IClientService;
import com.capibaracode.backend.util.mappers.ClientMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;

    private final CustomResponseBuilder customResponseBuilder;


    public ClientServiceImpl(ClientRepository clientRepository, CustomResponseBuilder customResponseBuilder) {
        this.clientRepository = clientRepository;
        this.customResponseBuilder = customResponseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findAll() {
        List<Client> clients = clientRepository.findAll();
        List<ClientResponse> responses = clients.stream().map(ClientMapper.INSTANCE::clientToClientResponse).toList();
        return customResponseBuilder.buildResponse(HttpStatus.OK, "Listado de todos los clientes", responses);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findAllIdentification() {
        List<String> identifications = clientRepository.findAllIdentification();
        return customResponseBuilder
                .buildResponse(HttpStatus.OK, "Listado de todas las identificaciones",identifications);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findAllOnlyActive() {
        List<Client> clients = clientRepository.findAllByStatusTrue();
        List<ClientResponse> responses = clients.stream().map(ClientMapper.INSTANCE::clientToClientResponse).toList();
        return customResponseBuilder.buildResponse(HttpStatus.OK, "Listado de todos los clientes activos", responses);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("No se encontro el cliente con id: " + id));
        ClientResponse response = ClientMapper.INSTANCE.clientToClientResponse(client);
        return customResponseBuilder.buildResponse(HttpStatus.OK, "Cliente encontrado", response);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findByIdentification(String identification) {
        Client client = clientRepository.findByIdentification(identification)
                .orElseThrow( () -> new RuntimeException("No se encontro el cliente con identificacion: " + identification));
        return customResponseBuilder
                .buildResponse(HttpStatus.OK, "Cliente encontrado", ClientMapper
                        .INSTANCE.clientToClientResponse(client));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(ClientRequest client) {
        Client clientToSave = ClientMapper.INSTANCE.clientRequestToClient(client);
        ClientResponse response = ClientMapper.INSTANCE.clientToClientResponse(clientRepository.save(clientToSave));
        return customResponseBuilder.buildResponse(HttpStatus.CREATED, "Cliente creado", response);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, ClientRequest client) {
        Client clientToUpdate = clientRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("No se encontro el cliente con id: " + id));
        clientToUpdate.setFullname(client.getFullname());
        clientToUpdate.setEmail(client.getEmail());
        clientToUpdate.setTelephone(client.getTelephone());
        clientToUpdate.setAddress(client.getAddress());
        clientToUpdate.setStatus(client.getStatus());
        clientToUpdate.setIdentification(client.getIdentification());
        clientToUpdate.setIdentificationType(client.getIdentificationType());
        ClientResponse response = ClientMapper.INSTANCE.clientToClientResponse(clientRepository.save(clientToUpdate));
        return customResponseBuilder.buildResponse(HttpStatus.OK, "Cliente actualizado", response);
    }

}
