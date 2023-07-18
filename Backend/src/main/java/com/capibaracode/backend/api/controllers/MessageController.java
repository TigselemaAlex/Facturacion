package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.Message;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping(value = "/protected/notification")
//@SecurityRequirement(name = "swagger")
public class MessageController {

    private final CustomResponseBuilder responseBuilder;

    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageController(CustomResponseBuilder responseBuilder, SimpMessagingTemplate simpMessagingTemplate) {
        this.responseBuilder = responseBuilder;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping(value = "/application")
    @SendTo(value = "/all/messages")
    public ResponseEntity<CustomAPIResponse<?>> send(final Message message){
        return responseBuilder.buildResponse(HttpStatus.OK, "Notificacion", message);
    }

    @MessageMapping(value = "/private")
    public void sendToSpecificUser(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
    }

}
