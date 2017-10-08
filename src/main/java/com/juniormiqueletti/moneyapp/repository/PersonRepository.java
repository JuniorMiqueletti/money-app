package com.juniormiqueletti.moneyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juniormiqueletti.moneyapp.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
