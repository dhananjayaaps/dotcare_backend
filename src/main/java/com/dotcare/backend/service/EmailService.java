package com.dotcare.backend.service;

import com.dotcare.backend.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(String recipientEmail, String verificationLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipientEmail);
        helper.setSubject("Email Verification");
        helper.setText("<html><body><p>Please verify your email by clicking the link below:</p>"
                + "<a href=\"" + verificationLink + "\">Verify Email</a></body></html>", true);

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String email, String resetLink) throws MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Email Verification");
        helper.setText("<html><body><p>Reset your password by clicking the link below:</p>"
                + "<a href=\"" + resetLink + "\">Verify Email</a></body></html>", true);

        mailSender.send(message);
    }
}
