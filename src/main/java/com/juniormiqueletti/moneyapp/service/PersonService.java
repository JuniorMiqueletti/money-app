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

	private PersonRepository repo;

    @Autowired
    public PersonService(PersonRepository repo) {
        this.repo = repo;
    }

    public Person update(Long id, Person person) {

		Person savedPerson = findPersonById(id); 
		BeanUtils.copyProperties(person, savedPerson, "id");

		return repo.save(savedPerson);
	}

	public void updatePropertyActive(Long id,Boolean active) {
		Person savedPerson = findPersonById(id);
		savedPerson.setActive(active);
		
		repo.save(savedPerson);
	}

	public Person findPersonById(Long id) {
		Optional<Person> savedPerson = repo.findById(id);
		
		if (!savedPerson.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		return savedPerson.get();
	}
}
