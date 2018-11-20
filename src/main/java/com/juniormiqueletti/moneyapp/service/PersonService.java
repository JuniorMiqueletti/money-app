package com.juniormiqueletti.moneyapp.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.juniormiqueletti.moneyapp.model.Person;
import com.juniormiqueletti.moneyapp.repository.PersonRepository;

import java.util.Optional;

@Service
public class PersonService {

	private PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repo) {
        this.repository = repo;
    }

    public Person update(final Long id, final Person person) {

		Person savedPerson = findPersonById(id); 
		BeanUtils.copyProperties(person, savedPerson, "id");

        person.getContacts().forEach(c -> c.setPerson(person));

		return repository.save(savedPerson);
	}

	public void updatePropertyActive(final Long id, final Boolean active) {
		Person savedPerson = findPersonById(id);
		savedPerson.setActive(active);
		
		repository.save(savedPerson);
	}

	public Person findPersonById(final Long id) {
		Optional<Person> savedPerson = repository.findById(id);
		
		if (!savedPerson.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		return savedPerson.get();
	}

    public Person save(final Person person) {
        person.getContacts().forEach(c -> c.setPerson(person));
        return repository.save(person);
    }
}
