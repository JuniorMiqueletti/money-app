package com.juniormiqueletti.moneyapp.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juniormiqueletti.moneyapp.model.Person;
import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.PersonRepository;
import com.juniormiqueletti.moneyapp.repository.ReleaseRepository;
import com.juniormiqueletti.moneyapp.service.exception.PersonInexistsOrInactiveException;

@Service
public class ReleaseService {

	@Autowired
	private ReleaseRepository repository;

	@Autowired
	private PersonRepository personRepository;

	public Release save(Release release) {

		Person person = personRepository.findOne(release.getPerson().getId());
		if (person == null || person.isInactive()) {
			throw new PersonInexistsOrInactiveException();
		}
		return repository.save(release);
	}

	public void delete(Long id) {
		repository.delete(id);
	}

	public Release update(Long id, Release release) {
		Release releaseSaved = findExistingRelease(id);
		if (!release.getPerson().equals(releaseSaved.getPerson())) {
			validatePerson(release);
		}

		BeanUtils.copyProperties(release, releaseSaved, "id");

		return repository.save(releaseSaved);
	}

	private void validatePerson(Release release) {
		Person person = null;
		if (release.getPerson().getId() != null) {
			person = personRepository.findOne(release.getPerson().getId());
		}

		if (person == null || person.isInactive()) {
			throw new PersonInexistsOrInactiveException();
		}
	}

	private Release findExistingRelease(Long id) {
		Release release = repository.findOne(id);
		if (release == null) {
			throw new IllegalArgumentException();
		}
		return release;
	}
}
