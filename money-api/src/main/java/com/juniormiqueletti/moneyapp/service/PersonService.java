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
		Person savedPerson = repo.findOne(id);

		if (savedPerson == null) {
			throw new EmptyResultDataAccessException(1);
		} else {
			BeanUtils.copyProperties(person, savedPerson, "id");
			return repo.save(savedPerson);
		}
	}

}
