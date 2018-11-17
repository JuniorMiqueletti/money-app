package com.juniormiqueletti.moneyapp.mail;

import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReleasesMailer extends Mailer {

    public static final String TEMPLATE = "mail/warn-expired-releases";

    public void warnAboutExpiredReleases(
        final List<Release> releases,
        final List<User> recipients
    ) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("releases", releases);

        List<String> emails = recipients.stream()
            .map(u -> u.getEmail())
            .collect(Collectors.toList());

        this.sendMail(
            "juniormiqueletti@gmail.com",
            emails,
            "Expired releases",
            TEMPLATE,
            variables
        );
    }
}
