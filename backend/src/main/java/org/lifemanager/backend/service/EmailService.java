package org.lifemanager.backend.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException ;
}
