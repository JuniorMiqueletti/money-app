package com.juniormiqueletti.moneyapp.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.juniormiqueletti.moneyapp.model.Person;
import com.juniormiqueletti.moneyapp.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository repo;

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

	private Person findPersonById(Long id) {
		Person savedPerson = repo.findOne(id);
		
		if (savedPerson == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return savedPerson;
	}
}
