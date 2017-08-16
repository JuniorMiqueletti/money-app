package com.juniormiqueletti.moneyapp.service;

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
}
