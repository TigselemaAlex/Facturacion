package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.infraestructure.abstract_services.IEmailService;
import com.capibaracode.backend.util.Email.EmailDetails;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class EmailServiceImpl implements IEmailService{
    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMail(EmailDetails details) {
        try {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper= new MimeMessageHelper(mailMessage, true);
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setText(details.getMsgBody(),true);
            helper.setSubject(details.getSubject());
            javaMailSender.send(mailMessage);
        }catch (Exception e){
            logger.error("Error to send email :" + e.getMessage());
        }
    }
}
