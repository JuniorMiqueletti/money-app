package com.juniormiqueletti.moneyapp.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine thymeleaf;

    /*TODO REMOVE Simple test
	@Autowired
	private ReleaseRepository repo;

	@EventListener
	private void teste(ApplicationReadyEvent event) {
		String template = "mail/warn-expired-releases";

		List<Release> list = repo.findAll();

		Map<String, Object> vars = new HashMap<>();
        vars.put("releases", list);

		this.sendMail(
		    "juniormiqueletti@gmail.com",
            Arrays.asList("juniormiqueletti@gmail.com"),
            "Testing",
            template,
            vars
        );

		System.out.println("Sending mail done...");
	}*/

    public void sendMail(
        final String sender,
        final List<String> recipients,
        final String subject,
        final String template,
        final Map<String, Object> variables
    ) {
        Context context = new Context(new Locale("en", "US"));

        variables.entrySet().forEach(
            p -> context.setVariable(p.getKey(), p.getValue())
        );

        String message = thymeleaf.process(template, context);
        this.sendMail(sender, recipients, subject, message);
    }

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
