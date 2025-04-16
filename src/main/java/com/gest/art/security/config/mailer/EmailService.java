package com.gest.art.security.config.mailer;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }

    public void sendEmailDetail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mamadou.koneamed@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendSimpleMail(EmailDetails emailDetails) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("support-it@universe.com");
        mailMessage.setTo(emailDetails.getToEmail());
        mailMessage.setText(emailDetails.getBody());
        mailMessage.setSubject(emailDetails.getSubject());
        mailSender.send(mailMessage);
    }

    public void sendPlainTextEmail(String toAddress,
                                   String subject, String message) throws AddressException,
            MessagingException {
        final String username = "mamadou.koneamed@gmail.com";
        final String password = "dkgp jfdm doca ctbh";
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        // properties.put("mail.transport.protocol", "smtp");
        // properties.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        //properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.debug", "true");
        //
        // *** BEGIN CHANGE
        properties.put("mail.smtp.user", username);
        // Authenticating
//        Authenticator auth = new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        };
        // creates a new session, no Authenticator (will connect() later)
        // Session session = Session.getInstance(properties, auth);
        Session session = Session.getDefaultInstance(properties);
        // *** END CHANGE
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        // set plain text message
        msg.setText(message);
        // *** BEGIN CHANGE
        // sends the e-mail
        Transport t = session.getTransport("smtp");
        t.connect(username, password);
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
        // *** END CHANGE
    }
}
