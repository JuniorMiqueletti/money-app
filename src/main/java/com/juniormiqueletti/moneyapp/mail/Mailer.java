package com.juniormiqueletti.moneyapp.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    /* TODO REMOVE Simple test
    @EventListener
	private void teste(ApplicationReadyEvent event) {
		this.sendMail("testes.algaworks@gmail.com",
				Arrays.asList("juniormiqueletti@gmail.com"),
				"Testing", "Hello!<br/>Test ok.");
		System.out.println("Mail service shutdown...");
	}*/

    public void sendMail(
        final String sender,
        final List<String> recipients,
        final String subject,
        final String message
    ) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(recipients.toArray(new String[recipients.size()]));
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Send mail problem", e);
        }
    }
}
