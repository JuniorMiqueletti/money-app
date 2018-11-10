package com.juniormiqueletti.moneyapp.service;

import com.juniormiqueletti.moneyapp.dto.StatisticalReleasePerson;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juniormiqueletti.moneyapp.model.Person;
import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.PersonRepository;
import com.juniormiqueletti.moneyapp.repository.ReleaseRepository;
import com.juniormiqueletti.moneyapp.service.exception.PersonInexistsOrInactiveException;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class ReleaseService {

	private ReleaseRepository repository;
	private PersonRepository personRepository;

    @Autowired
    public ReleaseService(
        ReleaseRepository repository,
        PersonRepository personRepository
    ) {
        this.repository = repository;
        this.personRepository = personRepository;
    }

    public Release save(final Release release) {
		Optional<Person> person = personRepository.findById(release.getPerson().getId());

		if (!person.isPresent() || person.get().isInactive())
			throw new PersonInexistsOrInactiveException();

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
