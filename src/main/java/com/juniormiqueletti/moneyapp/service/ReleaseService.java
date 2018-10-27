package com.juniormiqueletti.moneyapp.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juniormiqueletti.moneyapp.model.Person;
import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.PersonRepository;
import com.juniormiqueletti.moneyapp.repository.ReleaseRepository;
import com.juniormiqueletti.moneyapp.service.exception.PersonInexistsOrInactiveException;

import java.util.Optional;

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
