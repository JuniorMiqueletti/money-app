package com.juniormiqueletti.moneyapp.service;

import com.amazonaws.services.s3.AmazonS3;
import com.juniormiqueletti.moneyapp.dto.StatisticalReleasePerson;
import com.juniormiqueletti.moneyapp.mail.Mailer;
import com.juniormiqueletti.moneyapp.mail.ReleasesMailer;
import com.juniormiqueletti.moneyapp.model.User;
import com.juniormiqueletti.moneyapp.repository.UserRepository;
import com.juniormiqueletti.moneyapp.storage.AmazonS3Storage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.juniormiqueletti.moneyapp.model.Person;
import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.PersonRepository;
import com.juniormiqueletti.moneyapp.repository.ReleaseRepository;
import com.juniormiqueletti.moneyapp.service.exception.PersonInexistsOrInactiveException;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.util.StringUtils.*;

@Service
public class ReleaseService {

    private static final String ROLE = "ROLE_SEARCH_RELEASE";
    private Logger logger = LoggerFactory.getLogger(ReleaseService.class);

    private ReleaseRepository repository;
    private PersonRepository personRepository;
    private UserRepository userRepository;
    private ReleasesMailer mailer;
    private AmazonS3Storage s3Storage;

    @Autowired
    public ReleaseService(
        ReleaseRepository repository,
        PersonRepository personRepository,
        UserRepository userRepository,
        ReleasesMailer mailer,
        AmazonS3Storage s3Storage
    ) {
        this.repository = repository;
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.mailer = mailer;
        this.s3Storage = s3Storage;
    }

    public Release save(final Release release) {
		Optional<Person> person = personRepository.findById(release.getPerson().getId());

		if (!person.isPresent() || person.get().isInactive())
			throw new PersonInexistsOrInactiveException();

		if (hasText(release.getAttachment()))
		    s3Storage.save(release.getAttachment());

		return repository.save(release);
	}

	public void delete(final Long id) {
		repository.deleteById(id);
	}

	public Release update(final Long id, final Release release) {
        Release releaseSaved = findExistingRelease(id);
		if (!release.getPerson().equals(releaseSaved.getPerson())) {
			validatePerson(release);
		}

		if (isEmpty(release.getAttachment()) && hasText(releaseSaved.getAttachment())) {
            s3Storage.remove(releaseSaved.getAttachment());

		} else if (hasLength(release.getAttachment())
            && !release.getAttachment().equals(releaseSaved.getAttachment())) {
            s3Storage.replace(releaseSaved.getAttachment(), release.getAttachment());
        }

		BeanUtils.copyProperties(release, releaseSaved, "id");

		return repository.save(releaseSaved);
	}

    public byte[] reportByPerson(final LocalDate start, final LocalDate end) throws JRException {
        List<StatisticalReleasePerson> data = repository.byPerson(start, end);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("DT_START", Date.valueOf(start));
        parameters.put("DT_END", Date.valueOf(end));
        parameters.put("REPORT LOCAL", new Locale("en", "US"));

        InputStream inputStream = this.getClass().getResourceAsStream("/reports/releases-by-person.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(
            inputStream,
            parameters,
            new JRBeanCollectionDataSource(data)
        );

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void warnExpiredReleases() {
        if (logger.isDebugEnabled())
            logger.debug("Warning expired releases by e-mail.");

        List<Release> expireds =
            repository
                .findByDueDateLessThanEqualAndPayDateIsNull(LocalDate.now());

        if (expireds.isEmpty()) {
            logger.info("No expired releases to warn by e-mail");
            return;
        }

        logger.info("There are {} expired releases to warn by e-mail", expireds.size());

        List<User> usersRecipients =
            userRepository
                .findByPermissionListDescription(ROLE);

        if (usersRecipients.isEmpty()) {
            logger.warn("There are expired releases but, there aren't user recipients to send.");
            return;
        }

        mailer.warnAboutExpiredReleases(expireds, usersRecipients);

        logger.info("Email sent successfully.");
    }

	private void validatePerson(final Release release) {
		Optional<Person> person = null;
		if (release.getPerson().getId() != null) {
			person = personRepository.findById(release.getPerson().getId());
		}

		if (!person.isPresent() || person.get().isInactive()) {
			throw new PersonInexistsOrInactiveException();
		}
	}

	private Release findExistingRelease(final Long id) {
		Optional<Release> release = repository.findById(id);
		if (!release.isPresent())
			throw new IllegalArgumentException();

		return release.get();
	}
}
