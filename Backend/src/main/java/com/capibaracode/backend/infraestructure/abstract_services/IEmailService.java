package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.util.Email.EmailDetails;

public interface IEmailService {

    void sendSimpleMail(EmailDetails details);
}
