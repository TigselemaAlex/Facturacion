package com.capibaracode.backend.util.mappers;


import com.capibaracode.backend.api.models.requests.ClientRequest;
import com.capibaracode.backend.api.models.responses.ClientResponse;
import com.capibaracode.backend.domain.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientResponse clientToClientResponse(Client client);

    Client clientRequestToClient(ClientRequest client);
}
