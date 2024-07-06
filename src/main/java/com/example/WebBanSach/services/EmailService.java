package com.example.WebBanSach.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }
<<<<<<< HEAD

    public void sendInvoiceEmail(Order order, String recipientEmail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setTo(recipientEmail);
        helper.setSubject("Invoice for your order #" + order.getId());


        Context context = new Context();
        context.setVariable("order", order);


        String htmlContent = templateEngine.process("cart/invoice", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
=======
>>>>>>> 310556227abeda31e28fcf5ab0eb3313fa3f79c6
}
